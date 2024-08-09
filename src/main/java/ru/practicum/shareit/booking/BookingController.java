package ru.practicum.shareit.booking;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;
import ru.practicum.shareit.booking.dto.BookingCreateDto;
import ru.practicum.shareit.booking.dto.BookingDto;
import ru.practicum.shareit.booking.mapper.BookingMapper;
import ru.practicum.shareit.booking.model.Booking;
import ru.practicum.shareit.booking.service.BookingService;

import java.util.ArrayList;
import java.util.Collection;

/**
 * TODO Sprint add-bookings.
 */
@RestController
@RequestMapping(path = "/bookings")
@RequiredArgsConstructor
public class BookingController {
    private final BookingService bookingService;

    @PostMapping
    public BookingDto addBooking(@RequestHeader("X-Sharer-User-Id") int bookerId, @Valid @RequestBody BookingCreateDto bookingCreateDto) {
        return BookingMapper.toBookingDto(bookingService.addBooking(bookerId, bookingCreateDto));
    }

    @PatchMapping("/{bookingId}")
    public BookingDto updateBooking(@RequestHeader("X-Sharer-User-Id") int ownerId, @PathVariable int bookingId, @RequestParam boolean approved) {
        return BookingMapper.toBookingDto(bookingService.updateBooking(ownerId, bookingId, approved));
    }

    @GetMapping("/{bookingId}")
    public BookingDto getBookingByUser(@RequestHeader("X-Sharer-User-Id") int userId, @PathVariable int bookingId) {
        return BookingMapper.toBookingDto(bookingService.getBookingByUser(userId, bookingId));
    }

    @GetMapping
    public Collection<BookingDto> getAllBookingForUser(@RequestHeader("X-Sharer-User-Id") int userId, @RequestParam(required = false) String state) {
        Collection<Booking> bookings = bookingService.getAllBookingForUser(userId, state);
        Collection<BookingDto> bookingsDto = new ArrayList<>();
        for (Booking booking : bookings) {
            bookingsDto.add(BookingMapper.toBookingDto(booking));
        }
        return bookingsDto;
    }

    @GetMapping("/owner")
    public Collection<BookingDto> getBookingForOwner(@RequestHeader("X-Sharer-User-Id") int ownerId, @RequestParam(required = false) String state) {
        Collection<Booking> bookings = bookingService.getBookingForOwner(ownerId, state);
        Collection<BookingDto> bookingsDto = new ArrayList<>();
        for (Booking booking : bookings) {
            bookingsDto.add(BookingMapper.toBookingDto(booking));
        }
        return bookingsDto;
    }


}
