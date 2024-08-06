package ru.practicum.shareit.booking.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ru.practicum.shareit.booking.Status;
import ru.practicum.shareit.booking.dto.BookingDto;
import ru.practicum.shareit.booking.mapper.BookingMapper;
import ru.practicum.shareit.booking.model.Booking;
import ru.practicum.shareit.booking.repository.BookingRepository;
import ru.practicum.shareit.exception.BadRequestException;
import ru.practicum.shareit.exception.InternalServerErrorException;
import ru.practicum.shareit.exception.NotFoundException;
import ru.practicum.shareit.item.service.ItemService;
import ru.practicum.shareit.user.service.UserService;

import java.time.LocalDateTime;
import java.util.Collection;

@Service
@RequiredArgsConstructor
public class BookingServiceImpl implements BookingService {
    private final BookingRepository bookingRepository;
    private final UserService userService;
    private final ItemService itemService;

    @Override
    public Booking addBooking(int bookerId, BookingDto bookingDto) {
        userService.checkUserExist(bookerId);
        itemService.checkItemAvailable(bookingDto.getItemId());

        if (itemService.getItemById(bookingDto.getItemId()).getOwner().getId() == userService.getUserById(bookerId).getId()) {
            throw new NotFoundException("itemId = " + bookingDto.getItemId() + " belongs to userId = " + bookerId);
        }

        LocalDateTime currentTime = LocalDateTime.now();
        if (bookingDto.getEnd().isBefore(currentTime)) {
            throw new BadRequestException("end time is before current time");
        }
        if (bookingDto.getEnd().isBefore(bookingDto.getStart())) {
            throw new BadRequestException("end time is before start time");
        }
        if (bookingDto.getStart().isBefore(currentTime)) {
            throw new BadRequestException("start time is before current time");
        }
        if (bookingDto.getStart().isEqual(bookingDto.getEnd())) {
            throw new BadRequestException("start time is equal end time");
        }
        checkBooking(bookingDto);
        bookingDto.setStatus(Status.WAITING);
        Booking booking = BookingMapper.toBookingModel(bookingDto, itemService.getItemById(bookingDto.getItemId()), userService.getUserById(bookerId));
        return bookingRepository.save(booking);
    }

    @Override
    public Booking updateBooking(int ownerId, int bookingId, boolean approved) {
        Booking foundedBooking = bookingRepository.getBookingByOwner(ownerId, bookingId);
        if (foundedBooking == null) {
            throw new InternalServerErrorException("owner or booking not found");
        }
        if (approved) {
            if (foundedBooking.getStatus().equals(Status.APPROVED)) {
                throw new InternalServerErrorException("status has been already approved");
            } else {
                foundedBooking.setStatus(Status.APPROVED);
            }
        } else {
            foundedBooking.setStatus(Status.REJECTED);
        }
        return bookingRepository.save(foundedBooking);
    }

    @Override
    public Booking getBookingByUser(int userId, int bookingId) {
        if (!bookingRepository.findById(bookingId).isPresent()) {
            throw new NotFoundException("bookingId = " + bookingId + " not found");
        }
        if (bookingRepository.getBookingByBooker(userId, bookingId) != null) {
            return bookingRepository.getBookingByBooker(userId, bookingId);
        } else if (bookingRepository.getBookingByOwner(userId, bookingId) != null) {
            return bookingRepository.getBookingByOwner(userId, bookingId);
        } else {
            throw new NotFoundException("userId  = " + userId + " is not owner or booker");
        }
    }

    @Override
    public Collection<Booking> getAllBookingForUser(int userId, String state) {
        userService.checkUserExist(userId);
        if (state == null || state.equals("ALL")) {
            return bookingRepository.getAllForUser(userId);
        } else if (state.equals("CURRENT")) {
            return bookingRepository.getAllForUserWithStateCurrent(userId);
        } else if (state.equals("PAST")) {
            return bookingRepository.getAllForUserWithStatePast(userId);
        } else if (state.equals("FUTURE")) {
            return bookingRepository.getAllForUserWithStateFuture(userId);
        } else if (state.equals("WAITING")) {
            return bookingRepository.getAllForUserWithState(userId, Status.WAITING);
        } else if (state.equals("REJECTED")) {
            return bookingRepository.getAllForUserWithState(userId, Status.REJECTED);
        } else {
            throw new NotFoundException("Unknown state: UNSUPPORTED_STATUS");
        }
    }

    @Override
    public Collection<Booking> getBookingForOwner(int ownerId, String state) {
        userService.checkUserExist(ownerId);
        if (state == null || state.equals("ALL")) {
            return bookingRepository.getBookingForOwner(ownerId);
        } else if (state.equals("CURRENT")) {
            return bookingRepository.getBookingForOwnerWithStateCurrent(ownerId);
        } else if (state.equals("PAST")) {
            return bookingRepository.getBookingForOwnerWithStatePast(ownerId);
        } else if (state.equals("FUTURE")) {
            return bookingRepository.getBookingForOwnerWithStateFuture(ownerId);
        } else if (state.equals("WAITING")) {
            return bookingRepository.getBookingForOwnerWithState(ownerId, Status.WAITING);
        } else if (state.equals("REJECTED")) {
            return bookingRepository.getBookingForOwnerWithState(ownerId, Status.REJECTED);
        } else {
            throw new NotFoundException("Unknown state: UNSUPPORTED_STATUS");
        }
    }

    public void checkBooking(BookingDto bookingDto) {
        Booking currentApprovedBooking = bookingRepository.getCurrentApprovedBookingForItem(bookingDto.getItemId());
        Collection<Booking> futureApprovedBooking = bookingRepository.getFutureApprovedBookingForItem(bookingDto.getItemId());
        if (currentApprovedBooking != null) {
            if (currentApprovedBooking.getEnd().isAfter(bookingDto.getStart())) {
                throw new BadRequestException("item is available after " + currentApprovedBooking.getEnd() + " you should change start time");
            }
        }

        for (Booking booking : futureApprovedBooking) {
            if (bookingDto.getStart().isBefore(booking.getStart()) && bookingDto.getEnd().isBefore(booking.getStart())) {
                return;
            } else if (bookingDto.getStart().isBefore(booking.getStart()) && bookingDto.getEnd().isAfter(booking.getStart())) {
                throw new BadRequestException("item is not available from " + booking.getStart() + " you should change end time");
            } else if (bookingDto.getStart().isAfter(booking.getStart()) && bookingDto.getStart().isBefore(booking.getEnd())) {
                throw new BadRequestException("item is not available. Free time before " + booking.getStart() + " or after " + booking.getEnd());
            }
        }
    }
}
