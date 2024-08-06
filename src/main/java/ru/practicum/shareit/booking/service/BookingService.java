package ru.practicum.shareit.booking.service;

import ru.practicum.shareit.booking.dto.BookingDto;
import ru.practicum.shareit.booking.model.Booking;

import java.util.Collection;

public interface BookingService {
    Booking addBooking(int bookerId, BookingDto bookingDto);

    Booking updateBooking(int ownerId, int bookingId, boolean approved);

    Booking getBookingByUser(int userId, int bookingId);

    Collection<Booking> getAllBookingForUser(int userId, String state);

    Collection<Booking> getBookingForOwner(int ownerId, String state);

}