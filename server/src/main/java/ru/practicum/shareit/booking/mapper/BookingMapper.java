package ru.practicum.shareit.booking.mapper;

import ru.practicum.shareit.booking.Status;
import ru.practicum.shareit.booking.dto.BookingCreateDto;
import ru.practicum.shareit.booking.dto.BookingDto;
import ru.practicum.shareit.booking.model.Booking;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.user.User;

public class BookingMapper {

    public static Booking toBookingModel(BookingCreateDto bookingCreateDto, Item item, User user) {
        return new Booking(
                0,
                bookingCreateDto.getStart(),
                bookingCreateDto.getEnd(),
                item,
                user,
                Status.WAITING
        );
    }

    public static BookingDto toBookingDto(Booking booking) {
        return new BookingDto(
                booking.getId(),
                booking.getStart(),
                booking.getEnd(),
                booking.getItem(),
                booking.getBooker(),
                booking.getStatus()
        );
    }
}
