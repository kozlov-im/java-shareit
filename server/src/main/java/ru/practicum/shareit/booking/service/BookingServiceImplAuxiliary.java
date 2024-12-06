package ru.practicum.shareit.booking.service;

import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import ru.practicum.shareit.booking.dto.BookingCreateDto;
import ru.practicum.shareit.booking.model.Booking;
import ru.practicum.shareit.booking.repository.BookingRepository;
import ru.practicum.shareit.exception.BadRequestException;

import java.util.Collection;

@Service
@AllArgsConstructor
public class BookingServiceImplAuxiliary {

    BookingRepository bookingRepository;

    public void checkBooking(BookingCreateDto bookingCreateDto) {
        Booking currentApprovedBooking = bookingRepository.getCurrentApprovedBookingForItem(bookingCreateDto.getItemId());
        Collection<Booking> futureApprovedBooking = bookingRepository.getFutureApprovedBookingForItem(bookingCreateDto.getItemId());
        if (currentApprovedBooking != null) {
            if (currentApprovedBooking.getEnd().isAfter(bookingCreateDto.getStart())) {
                throw new BadRequestException("item is available after " + currentApprovedBooking.getEnd() + " you should change start time");
            }
        }

        for (Booking booking : futureApprovedBooking) {
            if (bookingCreateDto.getStart().isBefore(booking.getStart()) && bookingCreateDto.getEnd().isBefore(booking.getStart())) {
                return;
            } else if (bookingCreateDto.getStart().isBefore(booking.getStart()) && bookingCreateDto.getEnd().isAfter(booking.getStart())) {
                throw new BadRequestException("item is not available from " + booking.getStart() + " you should change end time");
            } else if (bookingCreateDto.getStart().isAfter(booking.getStart()) && bookingCreateDto.getStart().isBefore(booking.getEnd())) {
                throw new BadRequestException("item is not available. Free time before " + booking.getStart() + " or after " + booking.getEnd());
            }
        }
    }
}
