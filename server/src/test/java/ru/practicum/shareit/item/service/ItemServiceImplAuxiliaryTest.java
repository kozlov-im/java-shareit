package ru.practicum.shareit.item.service;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import ru.practicum.shareit.booking.Status;
import ru.practicum.shareit.booking.dto.BookingDto;
import ru.practicum.shareit.booking.model.Booking;
import ru.practicum.shareit.item.dto.CommentDto;
import ru.practicum.shareit.item.dto.ItemBookingDto;
import ru.practicum.shareit.item.model.Comment;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.item.repository.ItemRepository;
import ru.practicum.shareit.user.User;

import java.time.LocalDateTime;
import java.util.Collection;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class ItemServiceImplAuxiliaryTest {

    @Mock
    ItemRepository itemRepository;
    @InjectMocks
    ItemServiceImplAuxiliary itemServiceAuxiliary;

    @Test
    void createBookingForItem() {
        int userId = 1;
        int itemId = 1;
        User user1 = new User(1, "user1Name", "email1@mail.ru");
        User user2 = new User(2, "user2Name", "email2@mail.ru");
        User user3 = new User(3, "user3Name", "email3@mail.ru");
        Item item1 = new Item(1, "item1Name", "item1Description", true, user1, 0);

        Booking booking1 = new Booking(1, LocalDateTime.of(2024, 11, 20, 9, 1, 1),
                LocalDateTime.of(2024, 11, 20, 10, 1, 1),
                item1, user2, Status.APPROVED);
        Booking booking2 = new Booking(2, LocalDateTime.of(2024, 11, 21, 9, 1, 1),
                LocalDateTime.now().plusHours(2).withNano(0),
                item1, user3, Status.WAITING);


        BookingDto lastBookingDto = new BookingDto(1, LocalDateTime.of(2024, 11, 20, 9, 1, 1),
                LocalDateTime.of(2024, 11, 20, 10, 1, 1),
                item1, user2, Status.APPROVED);
        BookingDto nextBookingDto = new BookingDto(2, LocalDateTime.of(2024, 11, 21, 9, 1, 1),
                LocalDateTime.now().plusHours(2).withNano(0),
                item1, user3, Status.WAITING);


        CommentDto commentDto = new CommentDto(1, "comment", item1, "user2Name",
                LocalDateTime.of(2024, 11, 20, 11, 1, 1));
        Comment comment = new Comment(1, "comment", item1, user2,
                LocalDateTime.of(2024, 11, 20, 11, 1, 1));


        ItemBookingDto itemBookingDto = new ItemBookingDto(1, "item1Name",
                "item1Description",
                true, 0,
                lastBookingDto,
                nextBookingDto,
                List.of(new CommentDto(1, "comment", item1, "user2Name",
                        LocalDateTime.of(2024, 11, 20, 11, 1, 1))));


        Collection<Booking> itemBookings = List.of(booking1, booking2);
        Collection<CommentDto> commentsDto = List.of(commentDto);

        when(itemRepository.getReferenceById(itemId)).thenReturn(item1);

        ItemBookingDto returnedItemBookingDto = itemServiceAuxiliary.createBookingForItem(itemId, itemBookings, commentsDto);
        assertEquals(itemBookingDto, returnedItemBookingDto);
    }
}