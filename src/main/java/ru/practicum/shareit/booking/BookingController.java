package ru.practicum.shareit.booking;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;
import ru.practicum.shareit.booking.dto.BookingDto;
import ru.practicum.shareit.booking.model.Booking;
import ru.practicum.shareit.booking.service.BookingService;

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
    public Booking addBooking(@RequestHeader("X-Sharer-User-Id") int bookerId, @Valid @RequestBody BookingDto bookingDto) {
        return bookingService.addBooking(bookerId, bookingDto);
    }

    @PatchMapping("/{bookingId}")
    public Booking updateBooking(@RequestHeader("X-Sharer-User-Id") int ownerId, @PathVariable int bookingId, @RequestParam boolean approved) {
        return bookingService.updateBooking(ownerId, bookingId, approved);
    }

    @GetMapping("/{bookingId}")
    public Booking getBookingByUser(@RequestHeader("X-Sharer-User-Id") int userId, @PathVariable int bookingId) {
        return bookingService.getBookingByUser(userId, bookingId);
    }

    @GetMapping
    public Collection<Booking> getAllBookingForUser(@RequestHeader("X-Sharer-User-Id") int userId, @RequestParam(required = false) String state) {
        return bookingService.getAllBookingForUser(userId, state);
    }

    @GetMapping("/owner")
    public Collection<Booking> getBookingForOwner(@RequestHeader("X-Sharer-User-Id") int ownerId, @RequestParam(required = false) String state) {
        return bookingService.getBookingForOwner(ownerId, state);
    }


}
