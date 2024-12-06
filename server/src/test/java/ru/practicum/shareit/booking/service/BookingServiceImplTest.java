package ru.practicum.shareit.booking.service;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import ru.practicum.shareit.booking.Status;
import ru.practicum.shareit.booking.dto.BookingCreateDto;
import ru.practicum.shareit.booking.model.Booking;
import ru.practicum.shareit.booking.repository.BookingRepository;
import ru.practicum.shareit.exception.BadRequestException;
import ru.practicum.shareit.exception.InternalServerErrorException;
import ru.practicum.shareit.exception.NotFoundException;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.item.service.ItemService;
import ru.practicum.shareit.user.User;
import ru.practicum.shareit.user.service.UserService;

import java.time.LocalDateTime;
import java.util.Collection;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class BookingServiceImplTest {
    @Mock
    BookingRepository bookingRepository;
    @Mock
    UserService userService;
    @Mock
    ItemService itemService;
    @Mock
    BookingServiceImplAuxiliary bookingServiceAuxiliary;
    @InjectMocks
    BookingServiceImpl bookingService;

    @Test
    void addBooking_whenBookerIsOwner_thenExceptionThrows() {
        int userId = 1;
        int itemId = 1;
        User user1 = new User(1, "user1Name", "email1@mail.ru");
        Item item1 = new Item(1, "item1Name", "item1Description", true, user1, 0);
        BookingCreateDto bookingCreateDto = new BookingCreateDto(
                LocalDateTime.of(2024, 11, 20, 9, 1, 1),
                LocalDateTime.of(2024, 11, 20, 10, 1, 1),
                itemId);

        doNothing().when(userService).checkUserExist(userId);
        doNothing().when(itemService).checkItemAvailable(itemId);

        when(itemService.getItemById(itemId)).thenReturn(item1);
        when(userService.getUserById(userId)).thenReturn(user1);

        assertThrows(NotFoundException.class, () -> bookingService.addBooking(userId, bookingCreateDto));
    }

    @Test
    void addBooking_whenEndTimeBeforeCurrentTime_thenExceptionThrows() {
        int bookerId = 2;
        int itemId = 1;
        User user1 = new User(1, "user1Name", "email1@mail.ru");
        User user2 = new User(2, "user2Name", "email2@mail.ru");
        Item item1 = new Item(1, "item1Name", "item1Description", true, user1, 0);
        BookingCreateDto bookingCreateDto = new BookingCreateDto(
                LocalDateTime.now().plusHours(1),
                LocalDateTime.now().minusHours(2),
                itemId);

        doNothing().when(userService).checkUserExist(bookerId);
        doNothing().when(itemService).checkItemAvailable(itemId);

        when(itemService.getItemById(itemId)).thenReturn(item1);
        when(userService.getUserById(bookerId)).thenReturn(user2);
        BadRequestException exception =
                assertThrows(BadRequestException.class, () -> bookingService.addBooking(bookerId, bookingCreateDto));
        assertEquals("end time is before current time", exception.getMessage());
    }

    @Test
    void addBooking_whenEndTimeBeforeStartTime_thenExceptionThrows() {
        int bookerId = 2;
        int itemId = 1;
        User user1 = new User(1, "user1Name", "email1@mail.ru");
        User user2 = new User(2, "user2Name", "email2@mail.ru");
        Item item1 = new Item(1, "item1Name", "item1Description", true, user1, 0);
        BookingCreateDto bookingCreateDto = new BookingCreateDto(
                LocalDateTime.now().plusHours(2),
                LocalDateTime.now().plusHours(1),
                itemId);

        doNothing().when(userService).checkUserExist(bookerId);
        doNothing().when(itemService).checkItemAvailable(itemId);

        when(itemService.getItemById(itemId)).thenReturn(item1);
        when(userService.getUserById(bookerId)).thenReturn(user2);
        BadRequestException exception =
                assertThrows(BadRequestException.class, () -> bookingService.addBooking(bookerId, bookingCreateDto));
        assertEquals("end time is before start time", exception.getMessage());
    }

    @Test
    void addBooking_whenStartTimeBeforeCurrentTime_thenExceptionThrows() {
        int bookerId = 2;
        int itemId = 1;
        User user1 = new User(1, "user1Name", "email1@mail.ru");
        User user2 = new User(2, "user2Name", "email2@mail.ru");
        Item item1 = new Item(1, "item1Name", "item1Description", true, user1, 0);
        BookingCreateDto bookingCreateDto = new BookingCreateDto(
                LocalDateTime.now().minusHours(2),
                LocalDateTime.now().plusHours(1),
                itemId);

        doNothing().when(userService).checkUserExist(bookerId);
        doNothing().when(itemService).checkItemAvailable(itemId);

        when(itemService.getItemById(itemId)).thenReturn(item1);
        when(userService.getUserById(bookerId)).thenReturn(user2);
        BadRequestException exception =
                assertThrows(BadRequestException.class, () -> bookingService.addBooking(bookerId, bookingCreateDto));
        assertEquals("start time is before current time", exception.getMessage());
    }

    @Test
    void addBooking_whenStartTimeEqualsEndTime_thenExceptionThrows() {
        int bookerId = 2;
        int itemId = 1;
        User user1 = new User(1, "user1Name", "email1@mail.ru");
        User user2 = new User(2, "user2Name", "email2@mail.ru");
        Item item1 = new Item(1, "item1Name", "item1Description", true, user1, 0);
        BookingCreateDto bookingCreateDto = new BookingCreateDto(
                LocalDateTime.now().withNano(0),
                LocalDateTime.now().withNano(0),
                itemId);

        doNothing().when(userService).checkUserExist(bookerId);
        doNothing().when(itemService).checkItemAvailable(itemId);

        when(itemService.getItemById(itemId)).thenReturn(item1);
        when(userService.getUserById(bookerId)).thenReturn(user2);
        BadRequestException exception =
                assertThrows(BadRequestException.class, () -> bookingService.addBooking(bookerId, bookingCreateDto));
        assertEquals("start time is equal end time", exception.getMessage());
    }

    @Test
    void addBooking() {
        int bookerId = 2;
        int itemId = 1;
        User user1 = new User(1, "user1Name", "email1@mail.ru");
        User user2 = new User(2, "user2Name", "email2@mail.ru");
        Item item1 = new Item(1, "item1Name", "item1Description", true, user1, 0);
        BookingCreateDto bookingCreateDto = new BookingCreateDto(
                LocalDateTime.now().plusHours(1).withNano(0),
                LocalDateTime.now().plusHours(2).withNano(0),
                itemId);

        Booking booking = new Booking(0,
                LocalDateTime.now().plusHours(1).withNano(0),
                LocalDateTime.now().plusHours(2).withNano(0),
                item1, user2, Status.WAITING);

        doNothing().when(userService).checkUserExist(bookerId);
        doNothing().when(itemService).checkItemAvailable(itemId);
        when(itemService.getItemById(itemId)).thenReturn(item1);
        when(userService.getUserById(bookerId)).thenReturn(user2);
        doNothing().when(bookingServiceAuxiliary).checkBooking(bookingCreateDto);
        when(bookingRepository.save(booking)).thenReturn(booking);

        Booking returnBooking = bookingService.addBooking(bookerId, bookingCreateDto);
        assertEquals(booking, returnBooking);
    }

    @Test
    void updateBooking_whenOwnerNotFound_thenExceptionThrows() {
        int ownerId = 1;
        int bookingId = 1;

        when(bookingRepository.getBookingByOwner(ownerId, bookingId)).thenReturn(null);
        InternalServerErrorException exception =
                assertThrows(InternalServerErrorException.class,
                        () -> bookingService.updateBooking(ownerId, bookingId, "true"));
        assertEquals("owner or booking not found", exception.getMessage());
    }

    @Test
    void updateBooking_whenBookingAlreadyApproved_thenExceptionThrows() {
        int ownerId = 1;
        int bookingId = 1;
        User user1 = new User(1, "user1Name", "email1@mail.ru");
        User user2 = new User(2, "user2Name", "email2@mail.ru");
        Item item1 = new Item(1, "item1Name", "item1Description", true, user1, 0);
        Booking booking = new Booking(1,
                LocalDateTime.now().plusHours(1).withNano(0),
                LocalDateTime.now().plusHours(2).withNano(0),
                item1, user2, Status.APPROVED);

        when(bookingRepository.getBookingByOwner(ownerId, bookingId)).thenReturn(booking);
        InternalServerErrorException exception =
                assertThrows(InternalServerErrorException.class,
                        () -> bookingService.updateBooking(ownerId, bookingId, "true"));
        assertEquals("status has been already approved", exception.getMessage());
    }

    @Test
    void updateBooking_setApprovedIsTrue_thenSave() {
        int ownerId = 1;
        int bookingId = 1;
        User user1 = new User(1, "user1Name", "email1@mail.ru");
        User user2 = new User(2, "user2Name", "email2@mail.ru");
        Item item1 = new Item(1, "item1Name", "item1Description", true, user1, 0);
        Booking booking = new Booking(1,
                LocalDateTime.now().plusHours(1).withNano(0),
                LocalDateTime.now().plusHours(2).withNano(0),
                item1, user2, Status.WAITING);
        Booking bookingApproved = new Booking(1,
                LocalDateTime.now().plusHours(1).withNano(0),
                LocalDateTime.now().plusHours(2).withNano(0),
                item1, user2, Status.APPROVED);

        when(bookingRepository.getBookingByOwner(ownerId, bookingId)).thenReturn(booking);
        when(bookingRepository.save(bookingApproved)).thenReturn(bookingApproved);
        Booking returnedBooking = bookingService.updateBooking(ownerId, bookingId, "true");

        assertEquals(bookingApproved, returnedBooking);
    }

    @Test
    void updateBooking_setReject_thenSave() {
        int ownerId = 1;
        int bookingId = 1;
        User user1 = new User(1, "user1Name", "email1@mail.ru");
        User user2 = new User(2, "user2Name", "email2@mail.ru");
        Item item1 = new Item(1, "item1Name", "item1Description", true, user1, 0);
        Booking booking = new Booking(1,
                LocalDateTime.now().plusHours(1).withNano(0),
                LocalDateTime.now().plusHours(2).withNano(0),
                item1, user2, Status.WAITING);
        Booking bookingRejected = new Booking(1,
                LocalDateTime.now().plusHours(1).withNano(0),
                LocalDateTime.now().plusHours(2).withNano(0),
                item1, user2, Status.REJECTED);

        when(bookingRepository.getBookingByOwner(ownerId, bookingId)).thenReturn(booking);
        when(bookingRepository.save(bookingRejected)).thenReturn(bookingRejected);
        Booking returnedBooking = bookingService.updateBooking(ownerId, bookingId, "false");

        assertEquals(bookingRejected, returnedBooking);
    }

    @Test
    void getBookingByUser_whenUserIsNotFound_thenExceptionThrows() {
        int userId = 1;
        int bookingId = 1;

        when(bookingRepository.findById(userId)).thenReturn(Optional.empty());
        NotFoundException exception =
                assertThrows(NotFoundException.class,
                        () -> bookingService.getBookingByUser(userId, bookingId));
        assertEquals("bookingId = 1 not found", exception.getMessage());
    }

    @Test
    void getBookingByUser_whenUserIsFound_thenSave() {
        int userId = 1;
        int bookingId = 1;

        User user1 = new User(1, "user1Name", "email1@mail.ru");
        User user2 = new User(2, "user2Name", "email2@mail.ru");
        Item item1 = new Item(1, "item1Name", "item1Description", true, user1, 0);
        Booking booking = new Booking(1,
                LocalDateTime.now().plusHours(1).withNano(0),
                LocalDateTime.now().plusHours(2).withNano(0),
                item1, user2, Status.WAITING);

        when(bookingRepository.findById(userId)).thenReturn(Optional.of(booking));
        when(bookingRepository.getBookingByBooker(userId, bookingId)).thenReturn(booking);

        Booking returnedBooking = bookingService.getBookingByUser(userId, bookingId);
        assertEquals(booking, returnedBooking);
    }

    @Test
    void getBookingByUser_whenOwnerIsFound_thenSave() {
        int userId = 1;
        int bookingId = 1;

        User user1 = new User(1, "user1Name", "email1@mail.ru");
        User user2 = new User(2, "user2Name", "email2@mail.ru");
        Item item1 = new Item(1, "item1Name", "item1Description", true, user1, 0);
        Booking booking = new Booking(1,
                LocalDateTime.now().plusHours(1).withNano(0),
                LocalDateTime.now().plusHours(2).withNano(0),
                item1, user2, Status.WAITING);

        when(bookingRepository.findById(userId)).thenReturn(Optional.of(booking));
        when(bookingRepository.getBookingByBooker(userId, bookingId)).thenReturn(null);
        when(bookingRepository.getBookingByOwner(userId, bookingId)).thenReturn(booking);

        Booking returnedBooking = bookingService.getBookingByUser(userId, bookingId);
        assertEquals(booking, returnedBooking);
    }

    @Test
    void getBookingByUser_whenUserIsNotOwnerOrBooker_thenExceptionThrows() {
        int userId = 1;
        int bookingId = 1;

        User user1 = new User(1, "user1Name", "email1@mail.ru");
        User user2 = new User(2, "user2Name", "email2@mail.ru");
        Item item1 = new Item(1, "item1Name", "item1Description", true, user1, 0);
        Booking booking = new Booking(1,
                LocalDateTime.now().plusHours(1).withNano(0),
                LocalDateTime.now().plusHours(2).withNano(0),
                item1, user2, Status.WAITING);

        when(bookingRepository.findById(userId)).thenReturn(Optional.of(booking));
        when(bookingRepository.getBookingByBooker(userId, bookingId)).thenReturn(null);
        when(bookingRepository.getBookingByOwner(userId, bookingId)).thenReturn(null);

        NotFoundException exception =
                assertThrows(NotFoundException.class,
                        () -> bookingService.getBookingByUser(userId, bookingId));
        assertEquals("userId  = 1 is not owner or booker", exception.getMessage());
    }

    @Test
    void getAllBookingForUser_whenStateNullOrAll() {
        int userId = 1;
        User user1 = new User(1, "user1Name", "email1@mail.ru");
        User user2 = new User(2, "user2Name", "email2@mail.ru");
        Item item1 = new Item(1, "item1Name", "item1Description", true, user1, 0);
        Booking booking = new Booking(1,
                LocalDateTime.now().plusHours(1).withNano(0),
                LocalDateTime.now().plusHours(2).withNano(0),
                item1, user2, Status.WAITING);
        doNothing().when(userService).checkUserExist(userId);
        when(bookingRepository.getAllForUser(userId)).thenReturn(List.of(booking));

        Collection<Booking> returnBookings = bookingService.getAllBookingForUser(userId, "ALL");
        assertEquals(List.of(booking), returnBookings);
    }

    @Test
    void getAllBookingForUser_whenStateCurrent() {
        int userId = 1;
        User user1 = new User(1, "user1Name", "email1@mail.ru");
        User user2 = new User(2, "user2Name", "email2@mail.ru");
        Item item1 = new Item(1, "item1Name", "item1Description", true, user1, 0);
        Booking booking = new Booking(1,
                LocalDateTime.now().minusHours(1).withNano(0),
                LocalDateTime.now().plusHours(2).withNano(0),
                item1, user2, Status.WAITING);
        doNothing().when(userService).checkUserExist(userId);
        when(bookingRepository.getAllForUserWithStateCurrent(userId)).thenReturn(List.of(booking));

        Collection<Booking> returnBookings = bookingService.getAllBookingForUser(userId, "CURRENT");
        assertEquals(List.of(booking), returnBookings);
    }

    @Test
    void getAllBookingForUser_whenStatePast() {
        int userId = 1;
        User user1 = new User(1, "user1Name", "email1@mail.ru");
        User user2 = new User(2, "user2Name", "email2@mail.ru");
        Item item1 = new Item(1, "item1Name", "item1Description", true, user1, 0);
        Booking booking = new Booking(1,
                LocalDateTime.now().minusHours(3).withNano(0),
                LocalDateTime.now().minusHours(1).withNano(0),
                item1, user2, Status.WAITING);
        doNothing().when(userService).checkUserExist(userId);
        when(bookingRepository.getAllForUserWithStatePast(userId)).thenReturn(List.of(booking));

        Collection<Booking> returnBookings = bookingService.getAllBookingForUser(userId, "PAST");
        assertEquals(List.of(booking), returnBookings);
    }

    @Test
    void getAllBookingForUser_whenStateFuture() {
        int userId = 1;
        User user1 = new User(1, "user1Name", "email1@mail.ru");
        User user2 = new User(2, "user2Name", "email2@mail.ru");
        Item item1 = new Item(1, "item1Name", "item1Description", true, user1, 0);
        Booking booking = new Booking(1,
                LocalDateTime.now().plusHours(1).withNano(0),
                LocalDateTime.now().plusHours(3).withNano(0),
                item1, user2, Status.WAITING);
        doNothing().when(userService).checkUserExist(userId);
        when(bookingRepository.getAllForUserWithStateFuture(userId)).thenReturn(List.of(booking));

        Collection<Booking> returnBookings = bookingService.getAllBookingForUser(userId, "FUTURE");
        assertEquals(List.of(booking), returnBookings);
    }

    @Test
    void getAllBookingForUser_whenStateWaiting() {
        int userId = 1;
        User user1 = new User(1, "user1Name", "email1@mail.ru");
        User user2 = new User(2, "user2Name", "email2@mail.ru");
        Item item1 = new Item(1, "item1Name", "item1Description", true, user1, 0);
        Booking booking = new Booking(1,
                LocalDateTime.now().plusHours(1).withNano(0),
                LocalDateTime.now().plusHours(3).withNano(0),
                item1, user2, Status.WAITING);
        doNothing().when(userService).checkUserExist(userId);
        when(bookingRepository.getAllForUserWithState(userId, Status.WAITING)).thenReturn(List.of(booking));

        Collection<Booking> returnBookings = bookingService.getAllBookingForUser(userId, "WAITING");
        assertEquals(List.of(booking), returnBookings);
    }

    @Test
    void getAllBookingForUser_whenStateRejected() {
        int userId = 1;
        User user1 = new User(1, "user1Name", "email1@mail.ru");
        User user2 = new User(2, "user2Name", "email2@mail.ru");
        Item item1 = new Item(1, "item1Name", "item1Description", true, user1, 0);
        Booking booking = new Booking(1,
                LocalDateTime.now().plusHours(1).withNano(0),
                LocalDateTime.now().plusHours(3).withNano(0),
                item1, user2, Status.REJECTED);
        doNothing().when(userService).checkUserExist(userId);
        when(bookingRepository.getAllForUserWithState(userId, Status.REJECTED)).thenReturn(List.of(booking));

        Collection<Booking> returnBookings = bookingService.getAllBookingForUser(userId, "REJECTED");
        assertEquals(List.of(booking), returnBookings);
    }

    @Test
    void getAllBookingForUser_whenUnsupportedStatus_thenExceptionThrows() {
        int userId = 1;

        doNothing().when(userService).checkUserExist(userId);
        NotFoundException exception =
                assertThrows(NotFoundException.class,
                        () -> bookingService.getAllBookingForUser(userId, "UNKNOWN"));
        assertEquals("Unknown state: UNSUPPORTED_STATUS", exception.getMessage());

    }

    @Test
    void getBookingForOwner_whenStateIsNullOrAll() {
        int ownerId = 1;
        User user1 = new User(1, "user1Name", "email1@mail.ru");
        User user2 = new User(2, "user2Name", "email2@mail.ru");
        Item item1 = new Item(1, "item1Name", "item1Description", true, user1, 0);
        Booking booking = new Booking(1,
                LocalDateTime.now().plusHours(1).withNano(0),
                LocalDateTime.now().plusHours(2).withNano(0),
                item1, user2, Status.WAITING);
        doNothing().when(userService).checkUserExist(ownerId);
        when(bookingRepository.getBookingForOwner(ownerId)).thenReturn(List.of(booking));

        Collection<Booking> returnBookings = bookingService.getBookingForOwner(ownerId, "ALL");
        assertEquals(List.of(booking), returnBookings);
    }

    @Test
    void getBookingForOwner_whenStateCurrent() {
        int ownerId = 1;
        User user1 = new User(1, "user1Name", "email1@mail.ru");
        User user2 = new User(2, "user2Name", "email2@mail.ru");
        Item item1 = new Item(1, "item1Name", "item1Description", true, user1, 0);
        Booking booking = new Booking(1,
                LocalDateTime.now().minusHours(1).withNano(0),
                LocalDateTime.now().plusHours(2).withNano(0),
                item1, user2, Status.WAITING);
        doNothing().when(userService).checkUserExist(ownerId);
        when(bookingRepository.getBookingForOwnerWithStateCurrent(ownerId)).thenReturn(List.of(booking));

        Collection<Booking> returnBookings = bookingService.getBookingForOwner(ownerId, "CURRENT");
        assertEquals(List.of(booking), returnBookings);
    }

    @Test
    void getBookingForOwner_whenStatePast() {
        int ownerId = 1;
        User user1 = new User(1, "user1Name", "email1@mail.ru");
        User user2 = new User(2, "user2Name", "email2@mail.ru");
        Item item1 = new Item(1, "item1Name", "item1Description", true, user1, 0);
        Booking booking = new Booking(1,
                LocalDateTime.now().minusHours(3).withNano(0),
                LocalDateTime.now().minusHours(1).withNano(0),
                item1, user2, Status.WAITING);
        doNothing().when(userService).checkUserExist(ownerId);
        when(bookingRepository.getBookingForOwnerWithStatePast(ownerId)).thenReturn(List.of(booking));

        Collection<Booking> returnBookings = bookingService.getBookingForOwner(ownerId, "PAST");
        assertEquals(List.of(booking), returnBookings);
    }

    @Test
    void getBookingForOwner_whenStateFuture() {
        int ownerId = 1;
        User user1 = new User(1, "user1Name", "email1@mail.ru");
        User user2 = new User(2, "user2Name", "email2@mail.ru");
        Item item1 = new Item(1, "item1Name", "item1Description", true, user1, 0);
        Booking booking = new Booking(1,
                LocalDateTime.now().plusHours(1).withNano(0),
                LocalDateTime.now().plusHours(2).withNano(0),
                item1, user2, Status.WAITING);
        doNothing().when(userService).checkUserExist(ownerId);
        when(bookingRepository.getBookingForOwnerWithStateFuture(ownerId)).thenReturn(List.of(booking));

        Collection<Booking> returnBookings = bookingService.getBookingForOwner(ownerId, "FUTURE");
        assertEquals(List.of(booking), returnBookings);
    }

    @Test
    void getBookingForOwner_whenStateWaiting() {
        int ownerId = 1;
        User user1 = new User(1, "user1Name", "email1@mail.ru");
        User user2 = new User(2, "user2Name", "email2@mail.ru");
        Item item1 = new Item(1, "item1Name", "item1Description", true, user1, 0);
        Booking booking = new Booking(1,
                LocalDateTime.now().plusHours(1).withNano(0),
                LocalDateTime.now().plusHours(2).withNano(0),
                item1, user2, Status.WAITING);
        doNothing().when(userService).checkUserExist(ownerId);
        when(bookingRepository.getBookingForOwnerWithState(ownerId, Status.WAITING)).thenReturn(List.of(booking));

        Collection<Booking> returnBookings = bookingService.getBookingForOwner(ownerId, "WAITING");
        assertEquals(List.of(booking), returnBookings);
    }

    @Test
    void getBookingForOwner_whenStateRejected() {
        int ownerId = 1;
        User user1 = new User(1, "user1Name", "email1@mail.ru");
        User user2 = new User(2, "user2Name", "email2@mail.ru");
        Item item1 = new Item(1, "item1Name", "item1Description", true, user1, 0);
        Booking booking = new Booking(1,
                LocalDateTime.now().plusHours(1).withNano(0),
                LocalDateTime.now().plusHours(2).withNano(0),
                item1, user2, Status.REJECTED);
        doNothing().when(userService).checkUserExist(ownerId);
        when(bookingRepository.getBookingForOwnerWithState(ownerId, Status.REJECTED)).thenReturn(List.of(booking));

        Collection<Booking> returnBookings = bookingService.getBookingForOwner(ownerId, "REJECTED");
        assertEquals(List.of(booking), returnBookings);
    }

    @Test
    void getBookingForOwner_whenStateUnknown_thenExceptionThrows() {
        int ownerId = 1;
        doNothing().when(userService).checkUserExist(ownerId);
        NotFoundException exception =
                assertThrows(NotFoundException.class,
                        () -> bookingService.getBookingForOwner(ownerId, "UNKNOWN"));
        assertEquals("Unknown state: UNSUPPORTED_STATUS", exception.getMessage());
    }

}