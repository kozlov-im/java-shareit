package ru.practicum.shareit.booking.repository;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.jdbc.core.JdbcTemplate;
import ru.practicum.shareit.booking.Status;
import ru.practicum.shareit.booking.model.Booking;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.item.repository.ItemRepository;
import ru.practicum.shareit.user.User;
import ru.practicum.shareit.user.repository.UserRepository;

import java.time.LocalDateTime;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@DataJpaTest
class BookingRepositoryTest {
    @Autowired
    BookingRepository bookingRepository;
    @Autowired
    UserRepository userRepository;
    @Autowired
    ItemRepository itemRepository;
    @Autowired
    JdbcTemplate jdbcTemplate;
    User user1;
    User user2;
    Item item1;
    Item item2;
    Booking booking1;
    Booking booking2;

    @BeforeEach
    void setUp() {
        jdbcTemplate.execute("ALTER TABLE users ALTER COLUMN id RESTART WITH 1");
        jdbcTemplate.execute("ALTER TABLE items ALTER COLUMN id RESTART WITH 1");
        jdbcTemplate.execute("ALTER TABLE bookings ALTER COLUMN id RESTART WITH 1");

        user1 = new User(1, "userName1", "email1@mail.ru");
        user2 = new User(2, "userName2", "email2@mail.ru");
        userRepository.save(user1);
        userRepository.save(user2);

        item1 = new Item(1, "Item1", "Item1 description1", true, user1, 0);
        item2 = new Item(2, "Item2", "Item2 description2", true, user2, 0);
        itemRepository.save(item1);
        itemRepository.save(item2);

        booking1 = new Booking(1, LocalDateTime.of(2024, 11, 20, 9, 0, 0),
                LocalDateTime.of(2024, 11, 20, 10, 0, 0),
                item1, user2, Status.WAITING);
        booking2 = new Booking(2, LocalDateTime.of(2024, 11, 20, 11, 0, 0),
                LocalDateTime.of(2024, 11, 20, 12, 0, 0),
                item2, user1, Status.WAITING);
        bookingRepository.save(booking1);
        bookingRepository.save(booking2);
    }

    @Test
    void getBookingByOwner() {
        assertEquals(booking1, bookingRepository.getBookingByOwner(user1.getId(), booking1.getId()));
        assertNull(bookingRepository.getBookingByOwner(user2.getId(), booking1.getId()));
    }

    @Test
    void getBookingByBooker() {
        assertEquals(booking1, bookingRepository.getBookingByBooker(user2.getId(), booking1.getId()));
        assertNull(bookingRepository.getBookingByBooker(user1.getId(), booking1.getId()));
    }

    @Test
    void getAllForUser() {
        assertEquals(List.of(booking2, booking1), bookingRepository.getAllForUser(user2.getId()));
    }

    @Test
    void getAllForUserWithStateCurrent() {
    }

    @Test
    void getAllForUserWithStatePast() {
    }

    @Test
    void getAllForUserWithStateFuture() {
    }

    @Test
    void getAllForUserWithState() {
    }

    @Test
    void getCurrentApprovedBookingForItem() {
    }

    @Test
    void getFutureApprovedBookingForItem() {
    }

    @Test
    void getBookingForOwner() {
    }

    @Test
    void getBookingForOwnerWithStateCurrent() {
    }

    @Test
    void getBookingForOwnerWithStatePast() {
    }

    @Test
    void getBookingForOwnerWithStateFuture() {
    }

    @Test
    void getBookingForOwnerWithState() {
    }

    @Test
    void getBookingForItem() {
    }

    @Test
    void getApprovedBookingForBookerPast() {
    }
}