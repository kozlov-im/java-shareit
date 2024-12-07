package ru.practicum.shareit.booking.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ru.practicum.shareit.booking.State;
import ru.practicum.shareit.booking.Status;
import ru.practicum.shareit.booking.dto.BookingCreateDto;
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
    private final BookingServiceImplAuxiliary bookingServiceAuxiliary;

    @Override
    public Booking addBooking(int bookerId, BookingCreateDto bookingCreateDto) {
        userService.checkUserExist(bookerId);
        itemService.checkItemAvailable(bookingCreateDto.getItemId());

        if (itemService.getItemById(bookingCreateDto.getItemId()).getOwner().getId() == userService.getUserById(bookerId).getId()) {
            throw new NotFoundException("itemId = " + bookingCreateDto.getItemId() + " belongs to userId = " + bookerId);
        }

        LocalDateTime currentTime = LocalDateTime.now().withNano(0);
        if (bookingCreateDto.getEnd().isBefore(currentTime)) {
            throw new BadRequestException("end time is before current time");
        }
        if (bookingCreateDto.getEnd().isBefore(bookingCreateDto.getStart())) {
            throw new BadRequestException("end time is before start time");
        }
        if (bookingCreateDto.getStart().isBefore(currentTime)) {
            throw new BadRequestException("start time is before current time");
        }
        if (bookingCreateDto.getStart().isEqual(bookingCreateDto.getEnd())) {
            throw new BadRequestException("start time is equal end time");
        }
        bookingServiceAuxiliary.checkBooking(bookingCreateDto);
        Booking booking = BookingMapper.toBookingModel(bookingCreateDto, itemService.getItemById(bookingCreateDto.getItemId()), userService.getUserById(bookerId));
        return bookingRepository.save(booking);
    }

    @Override
    public Booking updateBooking(int ownerId, int bookingId, String approved) {
        Booking foundedBooking = bookingRepository.getBookingByOwner(ownerId, bookingId);
        if (foundedBooking == null) {
            throw new InternalServerErrorException("owner or booking not found");
        }
        if (approved.equals("true")) {
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
        if (state == null || state.equals(State.ALL.toString())) {
            return bookingRepository.getAllForUser(userId);
        } else if (state.equals(State.CURRENT.toString())) {
            return bookingRepository.getAllForUserWithStateCurrent(userId);
        } else if (state.equals(State.PAST.toString())) {
            return bookingRepository.getAllForUserWithStatePast(userId);
        } else if (state.equals(State.FUTURE.toString())) {
            return bookingRepository.getAllForUserWithStateFuture(userId);
        } else if (state.equals(Status.WAITING.toString())) {
            return bookingRepository.getAllForUserWithState(userId, Status.WAITING);
        } else if (state.equals(Status.REJECTED.toString())) {
            return bookingRepository.getAllForUserWithState(userId, Status.REJECTED);
        } else {
            throw new NotFoundException("Unknown state: UNSUPPORTED_STATUS");
        }
    }

    @Override
    public Collection<Booking> getBookingForOwner(int ownerId, String state) {
        userService.checkUserExist(ownerId);
        if (state == null || state.equals(State.ALL.toString())) {
            return bookingRepository.getBookingForOwner(ownerId);
        } else if (state.equals(State.CURRENT.toString())) {
            return bookingRepository.getBookingForOwnerWithStateCurrent(ownerId);
        } else if (state.equals(State.PAST.toString())) {
            return bookingRepository.getBookingForOwnerWithStatePast(ownerId);
        } else if (state.equals(State.FUTURE.toString())) {
            return bookingRepository.getBookingForOwnerWithStateFuture(ownerId);
        } else if (state.equals(Status.WAITING.toString())) {
            return bookingRepository.getBookingForOwnerWithState(ownerId, Status.WAITING);
        } else if (state.equals(Status.REJECTED.toString())) {
            return bookingRepository.getBookingForOwnerWithState(ownerId, Status.REJECTED);
        } else {
            throw new NotFoundException("Unknown state: UNSUPPORTED_STATUS");
        }
    }
}
