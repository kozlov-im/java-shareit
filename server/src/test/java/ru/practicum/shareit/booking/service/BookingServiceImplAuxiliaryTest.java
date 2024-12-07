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
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.user.User;

import java.time.LocalDateTime;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class BookingServiceImplAuxiliaryTest {
    @Mock
    BookingRepository bookingRepository;

    @InjectMocks
    BookingServiceImplAuxiliary bookingServiceAuxiliary;

    @Test
    void checkBooking_whenNewBookingCrossCurrentApprovedBooking_thenExceptionThrow() {
        int userId = 1;
        int itemId = 1;
        User user1 = new User(1, "user1Name", "email1@mail.ru");
        User user2 = new User(2, "user2Name", "email2@mail.ru");
        User user3 = new User(3, "user3Name", "email3@mail.ru");
        Item item1 = new Item(1, "item1Name", "item1Description", true, user1, 0);

        Booking booking1 = new Booking(1,
                LocalDateTime.now().minusHours(1).withNano(0),
                LocalDateTime.now().plusHours(2).withNano(0),
                item1, user2, Status.APPROVED);
        Booking booking2 = new Booking(2,
                LocalDateTime.now().plusHours(4).withNano(0),
                LocalDateTime.now().plusHours(6).withNano(0),
                item1, user3, Status.APPROVED);

        BookingCreateDto bookingCreateDto = new BookingCreateDto(
                LocalDateTime.now().plusHours(1).withNano(0),
                LocalDateTime.now().plusHours(2).withNano(0),
                itemId);


        when(bookingRepository.getCurrentApprovedBookingForItem(itemId)).thenReturn(booking1);
        when(bookingRepository.getFutureApprovedBookingForItem(itemId)).thenReturn(List.of(booking2));

        BadRequestException exception = assertThrows(BadRequestException.class,
                () -> bookingServiceAuxiliary.checkBooking(bookingCreateDto));
        System.out.println(exception.getMessage());

    }

    @Test
    void checkBooking_whenNewBookingStartBeforeApprovedBookingAndEndBeforeApprovedBooking_thenExceptionThrow() {
        int itemId = 1;
        User user1 = new User(1, "user1Name", "email1@mail.ru");
        User user2 = new User(2, "user2Name", "email2@mail.ru");
        User user3 = new User(3, "user3Name", "email3@mail.ru");
        Item item1 = new Item(1, "item1Name", "item1Description", true, user1, 0);

        Booking booking1 = new Booking(1,
                LocalDateTime.now().minusHours(1).withNano(0),
                LocalDateTime.now().plusHours(2).withNano(0),
                item1, user2, Status.APPROVED);
        Booking booking2 = new Booking(2,
                LocalDateTime.now().plusHours(4).withNano(0),
                LocalDateTime.now().plusHours(6).withNano(0),
                item1, user3, Status.APPROVED);

        BookingCreateDto bookingCreateDto = new BookingCreateDto(
                LocalDateTime.now().plusHours(3).withNano(0),
                LocalDateTime.now().plusHours(5).withNano(0),
                itemId);


        when(bookingRepository.getCurrentApprovedBookingForItem(itemId)).thenReturn(booking1);
        when(bookingRepository.getFutureApprovedBookingForItem(itemId)).thenReturn(List.of(booking2));

        BadRequestException exception = assertThrows(BadRequestException.class,
                () -> bookingServiceAuxiliary.checkBooking(bookingCreateDto));
        System.out.println(exception.getMessage());
    }

    @Test
    void checkBooking_whenNewBookingStartAfterApprovedAndEndBeforeEndApproved_thenExceptionThrow() {
        int itemId = 1;
        User user1 = new User(1, "user1Name", "email1@mail.ru");
        User user2 = new User(2, "user2Name", "email2@mail.ru");
        User user3 = new User(3, "user3Name", "email3@mail.ru");
        Item item1 = new Item(1, "item1Name", "item1Description", true, user1, 0);

        Booking booking1 = new Booking(1,
                LocalDateTime.now().minusHours(1).withNano(0),
                LocalDateTime.now().plusHours(2).withNano(0),
                item1, user2, Status.APPROVED);
        Booking booking2 = new Booking(2,
                LocalDateTime.now().plusHours(4).withNano(0),
                LocalDateTime.now().plusHours(8).withNano(0),
                item1, user3, Status.APPROVED);

        BookingCreateDto bookingCreateDto = new BookingCreateDto(
                LocalDateTime.now().plusHours(5).withNano(0),
                LocalDateTime.now().plusHours(6).withNano(0),
                itemId);


        when(bookingRepository.getCurrentApprovedBookingForItem(itemId)).thenReturn(booking1);
        when(bookingRepository.getFutureApprovedBookingForItem(itemId)).thenReturn(List.of(booking2));

        BadRequestException exception = assertThrows(BadRequestException.class,
                () -> bookingServiceAuxiliary.checkBooking(bookingCreateDto));
        System.out.println(exception.getMessage());
    }
}