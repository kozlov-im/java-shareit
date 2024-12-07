package ru.practicum.shareit.booking;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import ru.practicum.shareit.booking.dto.BookingCreateDto;
import ru.practicum.shareit.booking.dto.BookingState;

/**
 * TODO Sprint add-bookings.
 */
@Controller
@RequestMapping(path = "/bookings")
@RequiredArgsConstructor
@Slf4j
@Validated
public class BookingController {
	private final BookingClient bookingClient;

	@PostMapping
	public ResponseEntity<Object> addBooking(@RequestHeader("X-Sharer-User-Id") long bookerId,
											 @RequestBody @Valid BookingCreateDto bookingCreateDto) {
		log.info("Creating booking {}, userId={}", bookingCreateDto, bookerId);
		return bookingClient.addBooking(bookerId, bookingCreateDto);
	}

	@PatchMapping("/{bookingId}")
	public ResponseEntity<Object> updateBooking(@RequestHeader("X-Sharer-User-Id") long ownerId,
											 @PathVariable long bookingId, @RequestParam boolean approved) {
		log.info("Update booking bookingId={}", bookingId);
		return bookingClient.updateBooking(ownerId, bookingId, approved);
	}

	@GetMapping("/{bookingId}")
	public ResponseEntity<Object> getBookingByUser(@RequestHeader("X-Sharer-User-Id") long userId,
												   @PathVariable long bookingId) {
		log.info("Get booking bookingId ={} for userId={}", bookingId, userId);
		return bookingClient.getBookingByUser(userId, bookingId);
	}

	@GetMapping
	public ResponseEntity<Object> getAllBookingForUser(@RequestHeader("X-Sharer-User-Id") long userId,
													   @RequestParam(name = "state", defaultValue = "all") String stateParam) {
		BookingState state = BookingState.from(stateParam)
				.orElseThrow(() -> new IllegalArgumentException("Unknown state: " + stateParam));
		log.info("Get bookings for userId={}, state param = {}", userId, stateParam);
		return bookingClient.getBookings(userId, stateParam.toUpperCase());
	}

	@GetMapping("/owner")
	public ResponseEntity<Object> getBookingForOwner(@RequestHeader("X-Sharer-User-Id") long ownerId,
													 @RequestParam(name = "state", defaultValue = "all") String stateParam) {
		BookingState state = BookingState.from(stateParam)
				.orElseThrow(() -> new IllegalArgumentException("Unknown state: " + stateParam));
		log.info("Get bookings for ownerId={}", ownerId);
		return bookingClient.getBookings(ownerId, stateParam.toUpperCase());
	}

}
