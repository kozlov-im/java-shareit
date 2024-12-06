package ru.practicum.shareit.booking;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import ru.practicum.shareit.booking.dto.BookingCreateDto;
import ru.practicum.shareit.booking.dto.BookingDto;
import ru.practicum.shareit.booking.model.Booking;
import ru.practicum.shareit.booking.service.BookingService;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.user.User;

import java.nio.charset.StandardCharsets;
import java.time.LocalDateTime;
import java.util.List;

import static org.hamcrest.Matchers.is;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(controllers = BookingController.class)
class BookingControllerIT {

    @Autowired
    ObjectMapper mapper;
    @MockBean
    BookingService bookingService;

    @Autowired
    MockMvc mvc;

    @Test
    void addBooking() throws Exception {
        int userId = 2;
        int itemId = 1;
        User user1 = new User(1, "user1Name", "email1@mail.ru");
        User user2 = new User(2, "user2Name", "email2@mail.ru");
        Item item1 = new Item(1, "item1Name", "item1Description", true, user1, 0);

        BookingCreateDto bookingCreateDto = new BookingCreateDto(
                LocalDateTime.of(2024, 11, 20, 9, 1, 1),
                LocalDateTime.of(2024, 11, 20, 10, 1, 1),
                itemId);

        BookingDto bookingDto = new BookingDto(1, LocalDateTime.of(2024, 11, 20, 9, 1, 1),
                LocalDateTime.of(2024, 11, 20, 10, 1, 1),
                item1, user2, Status.WAITING);
        Booking booking = new Booking(1, LocalDateTime.of(2024, 11, 20, 9, 1, 1),
                LocalDateTime.of(2024, 11, 20, 10, 1, 1),
                item1, user2, Status.WAITING);
        when(bookingService.addBooking(userId, bookingCreateDto)).thenReturn(booking);

        mvc.perform(post("/bookings")
                        .content(mapper.writeValueAsString(bookingCreateDto))
                        .header("X-Sharer-User-Id", userId)
                        .characterEncoding(StandardCharsets.UTF_8)
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id", is(bookingDto.getId())))
                .andExpect(jsonPath("$.start", is(bookingDto.getStart().toString())))
                .andExpect(jsonPath("$.end", is(bookingDto.getEnd().toString())))
                .andExpect(jsonPath("$.item.id", is(bookingDto.getItem().getId())))
                .andExpect(jsonPath("$.item.name", is(bookingDto.getItem().getName())))
                .andExpect(jsonPath("$.item.description", is(bookingDto.getItem().getDescription())))
                .andExpect(jsonPath("$.item.available", is(bookingDto.getItem().getAvailable())))
                .andExpect(jsonPath("$.item.owner.id", is(bookingDto.getItem().getOwner().getId())))
                .andExpect(jsonPath("$.item.owner.name", is(bookingDto.getItem().getOwner().getName())))
                .andExpect(jsonPath("$.item.owner.email", is(bookingDto.getItem().getOwner().getEmail())))
                .andExpect(jsonPath("$.item.request", is(bookingDto.getItem().getRequest())))
                .andExpect(jsonPath("$.booker.id", is(bookingDto.getBooker().getId())))
                .andExpect(jsonPath("$.booker.name", is(bookingDto.getBooker().getName())))
                .andExpect(jsonPath("$.booker.email", is(bookingDto.getBooker().getEmail())))
                .andExpect(jsonPath("$.status", is(bookingDto.getStatus().toString())));
    }

    @Test
    void updateBooking() throws Exception {
        int ownerId = 1;
        int bookingId = 1;
        String approved = "true";
        User user1 = new User(1, "user1Name", "email1@mail.ru");
        User user2 = new User(2, "user2Name", "email2@mail.ru");
        Item item1 = new Item(1, "item1Name", "item1Description", true, user1, 0);

        BookingDto bookingDto = new BookingDto(1, LocalDateTime.of(2024, 11, 20, 9, 1, 1),
                LocalDateTime.of(2024, 11, 20, 10, 1, 1),
                item1, user2, Status.APPROVED);
        Booking booking = new Booking(1, LocalDateTime.of(2024, 11, 20, 9, 1, 1),
                LocalDateTime.of(2024, 11, 20, 10, 1, 1),
                item1, user2, Status.APPROVED);
        when(bookingService.updateBooking(ownerId, bookingId, approved)).thenReturn(booking);

        mvc.perform(patch("/bookings/{bookingId}", bookingId)
                        .header("X-Sharer-User-Id", ownerId)
                        .param("approved", approved)
                        .characterEncoding(StandardCharsets.UTF_8)
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id", is(bookingDto.getId())))
                .andExpect(jsonPath("$.start", is(bookingDto.getStart().toString())))
                .andExpect(jsonPath("$.end", is(bookingDto.getEnd().toString())))
                .andExpect(jsonPath("$.item.id", is(bookingDto.getItem().getId())))
                .andExpect(jsonPath("$.item.name", is(bookingDto.getItem().getName())))
                .andExpect(jsonPath("$.item.description", is(bookingDto.getItem().getDescription())))
                .andExpect(jsonPath("$.item.available", is(bookingDto.getItem().getAvailable())))
                .andExpect(jsonPath("$.item.owner.id", is(bookingDto.getItem().getOwner().getId())))
                .andExpect(jsonPath("$.item.owner.name", is(bookingDto.getItem().getOwner().getName())))
                .andExpect(jsonPath("$.item.owner.email", is(bookingDto.getItem().getOwner().getEmail())))
                .andExpect(jsonPath("$.item.request", is(bookingDto.getItem().getRequest())))
                .andExpect(jsonPath("$.booker.id", is(bookingDto.getBooker().getId())))
                .andExpect(jsonPath("$.booker.name", is(bookingDto.getBooker().getName())))
                .andExpect(jsonPath("$.booker.email", is(bookingDto.getBooker().getEmail())))
                .andExpect(jsonPath("$.status", is(bookingDto.getStatus().toString())));
    }

    @Test
    void getBookingByUser() throws Exception {
        int userId = 2;
        int bookingId = 1;
        User user1 = new User(1, "user1Name", "email1@mail.ru");
        User user2 = new User(2, "user2Name", "email2@mail.ru");
        Item item1 = new Item(1, "item1Name", "item1Description", true, user1, 0);

        BookingDto bookingDto = new BookingDto(1, LocalDateTime.of(2024, 11, 20, 9, 1, 1),
                LocalDateTime.of(2024, 11, 20, 10, 1, 1),
                item1, user2, Status.APPROVED);
        Booking booking = new Booking(1, LocalDateTime.of(2024, 11, 20, 9, 1, 1),
                LocalDateTime.of(2024, 11, 20, 10, 1, 1),
                item1, user2, Status.APPROVED);
        when(bookingService.getBookingByUser(userId, bookingId)).thenReturn(booking);

        mvc.perform(get("/bookings/{bookingId}", bookingId)
                        .header("X-Sharer-User-Id", userId)
                        .characterEncoding(StandardCharsets.UTF_8)
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id", is(bookingDto.getId())))
                .andExpect(jsonPath("$.start", is(bookingDto.getStart().toString())))
                .andExpect(jsonPath("$.end", is(bookingDto.getEnd().toString())))
                .andExpect(jsonPath("$.item.id", is(bookingDto.getItem().getId())))
                .andExpect(jsonPath("$.item.name", is(bookingDto.getItem().getName())))
                .andExpect(jsonPath("$.item.description", is(bookingDto.getItem().getDescription())))
                .andExpect(jsonPath("$.item.available", is(bookingDto.getItem().getAvailable())))
                .andExpect(jsonPath("$.item.owner.id", is(bookingDto.getItem().getOwner().getId())))
                .andExpect(jsonPath("$.item.owner.name", is(bookingDto.getItem().getOwner().getName())))
                .andExpect(jsonPath("$.item.owner.email", is(bookingDto.getItem().getOwner().getEmail())))
                .andExpect(jsonPath("$.item.request", is(bookingDto.getItem().getRequest())))
                .andExpect(jsonPath("$.booker.id", is(bookingDto.getBooker().getId())))
                .andExpect(jsonPath("$.booker.name", is(bookingDto.getBooker().getName())))
                .andExpect(jsonPath("$.booker.email", is(bookingDto.getBooker().getEmail())))
                .andExpect(jsonPath("$.status", is(bookingDto.getStatus().toString())));
    }

    @Test
    void getAllBookingForUser() throws Exception {
        int userId = 2;
        String state = "WAITING";
        User user1 = new User(1, "user1Name", "email1@mail.ru");
        User user2 = new User(2, "user2Name", "email2@mail.ru");
        Item item1 = new Item(1, "item1Name", "item1Description", true, user1, 0);

        BookingDto bookingDto = new BookingDto(1, LocalDateTime.of(2024, 11, 20, 9, 1, 1),
                LocalDateTime.of(2024, 11, 20, 10, 1, 1),
                item1, user2, Status.WAITING);
        Booking booking = new Booking(1, LocalDateTime.of(2024, 11, 20, 9, 1, 1),
                LocalDateTime.of(2024, 11, 20, 10, 1, 1),
                item1, user2, Status.WAITING);
        when(bookingService.getAllBookingForUser(userId, state)).thenReturn(List.of(booking));

        mvc.perform(get("/bookings")
                        .header("X-Sharer-User-Id", userId)
                        .param("state", state)
                        .characterEncoding(StandardCharsets.UTF_8)
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.[0].id", is(bookingDto.getId())))
                .andExpect(jsonPath("$.[0].start", is(bookingDto.getStart().toString())))
                .andExpect(jsonPath("$.[0].end", is(bookingDto.getEnd().toString())))
                .andExpect(jsonPath("$.[0].item.id", is(bookingDto.getItem().getId())))
                .andExpect(jsonPath("$.[0].item.name", is(bookingDto.getItem().getName())))
                .andExpect(jsonPath("$.[0].item.description", is(bookingDto.getItem().getDescription())))
                .andExpect(jsonPath("$.[0].item.available", is(bookingDto.getItem().getAvailable())))
                .andExpect(jsonPath("$.[0].item.owner.id", is(bookingDto.getItem().getOwner().getId())))
                .andExpect(jsonPath("$.[0].item.owner.name", is(bookingDto.getItem().getOwner().getName())))
                .andExpect(jsonPath("$.[0].item.owner.email", is(bookingDto.getItem().getOwner().getEmail())))
                .andExpect(jsonPath("$.[0].item.request", is(bookingDto.getItem().getRequest())))
                .andExpect(jsonPath("$.[0].booker.id", is(bookingDto.getBooker().getId())))
                .andExpect(jsonPath("$.[0].booker.name", is(bookingDto.getBooker().getName())))
                .andExpect(jsonPath("$.[0].booker.email", is(bookingDto.getBooker().getEmail())))
                .andExpect(jsonPath("$.[0].status", is(bookingDto.getStatus().toString())));
    }

    @Test
    void getBookingForOwner() throws Exception {
        int ownerId = 1;
        String state = "WAITING";
        User user1 = new User(1, "user1Name", "email1@mail.ru");
        User user2 = new User(2, "user2Name", "email2@mail.ru");
        Item item1 = new Item(1, "item1Name", "item1Description", true, user1, 0);

        BookingDto bookingDto = new BookingDto(1, LocalDateTime.of(2024, 11, 20, 9, 1, 1),
                LocalDateTime.of(2024, 11, 20, 10, 1, 1),
                item1, user2, Status.WAITING);
        Booking booking = new Booking(1, LocalDateTime.of(2024, 11, 20, 9, 1, 1),
                LocalDateTime.of(2024, 11, 20, 10, 1, 1),
                item1, user2, Status.WAITING);
        when(bookingService.getBookingForOwner(ownerId, state)).thenReturn(List.of(booking));

        mvc.perform(get("/bookings/owner")
                        .header("X-Sharer-User-Id", ownerId)
                        .param("state", state)
                        .characterEncoding(StandardCharsets.UTF_8)
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.[0].id", is(bookingDto.getId())))
                .andExpect(jsonPath("$.[0].start", is(bookingDto.getStart().toString())))
                .andExpect(jsonPath("$.[0].end", is(bookingDto.getEnd().toString())))
                .andExpect(jsonPath("$.[0].item.id", is(bookingDto.getItem().getId())))
                .andExpect(jsonPath("$.[0].item.name", is(bookingDto.getItem().getName())))
                .andExpect(jsonPath("$.[0].item.description", is(bookingDto.getItem().getDescription())))
                .andExpect(jsonPath("$.[0].item.available", is(bookingDto.getItem().getAvailable())))
                .andExpect(jsonPath("$.[0].item.owner.id", is(bookingDto.getItem().getOwner().getId())))
                .andExpect(jsonPath("$.[0].item.owner.name", is(bookingDto.getItem().getOwner().getName())))
                .andExpect(jsonPath("$.[0].item.owner.email", is(bookingDto.getItem().getOwner().getEmail())))
                .andExpect(jsonPath("$.[0].item.request", is(bookingDto.getItem().getRequest())))
                .andExpect(jsonPath("$.[0].booker.id", is(bookingDto.getBooker().getId())))
                .andExpect(jsonPath("$.[0].booker.name", is(bookingDto.getBooker().getName())))
                .andExpect(jsonPath("$.[0].booker.email", is(bookingDto.getBooker().getEmail())))
                .andExpect(jsonPath("$.[0].status", is(bookingDto.getStatus().toString())));
    }
}