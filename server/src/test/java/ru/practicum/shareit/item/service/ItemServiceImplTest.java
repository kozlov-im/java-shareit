package ru.practicum.shareit.item.service;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import ru.practicum.shareit.booking.Status;
import ru.practicum.shareit.booking.dto.BookingDto;
import ru.practicum.shareit.booking.model.Booking;
import ru.practicum.shareit.booking.repository.BookingRepository;
import ru.practicum.shareit.exception.BadRequestException;
import ru.practicum.shareit.exception.InternalServerErrorException;
import ru.practicum.shareit.exception.NotFoundException;
import ru.practicum.shareit.item.dto.CommentCreateDto;
import ru.practicum.shareit.item.dto.CommentDto;
import ru.practicum.shareit.item.dto.ItemCreateDto;
import ru.practicum.shareit.item.dto.ItemDtoBooking;
import ru.practicum.shareit.item.model.Comment;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.item.repository.CommentRepository;
import ru.practicum.shareit.item.repository.ItemRepository;
import ru.practicum.shareit.user.User;
import ru.practicum.shareit.user.service.UserService;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class ItemServiceImplTest {
    @Mock
    ItemRepository itemRepository;
    @Mock
    UserService userService;
    @Mock
    BookingRepository bookingRepository;
    @Mock
    CommentRepository commentRepository;

    @Mock
    ItemServiceImplAuxiliary itemServiceAuxiliary;
    @InjectMocks
    ItemServiceImpl itemService;

    @Test
    void createItem() {
        int userId = 1;
        User user = new User(1, "userName", "email@mail.ru");
        ItemCreateDto itemCreateDto = new ItemCreateDto("itemName", "itemDescription", true);
        Item item = new Item(0, "itemName", "itemDescription", true, user, 0);

        doNothing().when(userService).checkUserExist(userId);
        when(userService.getUserById(userId)).thenReturn(user);
        when(itemRepository.save(item)).thenReturn(item);

        Item returnedItem = itemService.createItem(userId, itemCreateDto);
        assertEquals(item, returnedItem);
        verify(itemRepository, times(1)).save(item);
    }

    @Test
    void updateItem() {
        int userId = 1;
        int itemId = 1;
        User user = new User(1, "userName", "email@mail.ru");
        ItemCreateDto itemUpdateCreateDto = new ItemCreateDto("itemNameUpdated", "itemDescriptionUpdated", true);
        Item item = new Item(0, "itemName", "itemDescription", true, user, 0);

        doNothing().when(userService).checkUserExist(userId);
        when(itemRepository.findById(itemId)).thenReturn(Optional.of(new Item()));
        when(itemRepository.save(item)).thenReturn(item);
        when(itemRepository.getItemForUser(userId, itemId)).thenReturn(item);

        Item returnedItem = itemService.updateItem(userId, itemId, itemUpdateCreateDto);
        assertEquals(item, returnedItem);
    }

    @Test
    void getItemById() {
        int itemId = 1;
        User user = new User(1, "userName", "email@mail.ru");
        Item item = new Item(0, "itemName", "itemDescription", true, user, 0);

        when(itemRepository.findById(itemId)).thenReturn(Optional.of(new Item()));
        when(itemRepository.getReferenceById(itemId)).thenReturn(item);

        Item returnedItem = itemService.getItemById(itemId);
        assertEquals(item, returnedItem);
    }

    @Test
    void getItemByIdAndUserId_whenUserIsOwnerForItem_themReturnWithLastAndNextBooking() {
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
                LocalDateTime.of(2024, 11, 21, 10, 1, 1),
                item1, user3, Status.WAITING);


        BookingDto lastBookingDto = new BookingDto(1, LocalDateTime.of(2024, 11, 20, 9, 1, 1),
                LocalDateTime.of(2024, 11, 20, 10, 1, 1),
                item1, user2, Status.APPROVED);
        BookingDto nextBookingDto = new BookingDto(2, LocalDateTime.of(2024, 11, 21, 9, 1, 1),
                LocalDateTime.of(2024, 11, 21, 10, 1, 1),
                item1, user3, Status.WAITING);


        CommentDto commentDto = new CommentDto(1, "comment", item1, "user2Name",
                LocalDateTime.of(2024, 11, 20, 11, 1, 1));
        Comment comment = new Comment(1, "comment", item1, user2,
                LocalDateTime.of(2024, 11, 20, 11, 1, 1));


        ItemDtoBooking itemDtoBooking = new ItemDtoBooking(1, "item1Name",
                "item1Description",
                true, 0,
                lastBookingDto,
                nextBookingDto,
                List.of(new CommentDto(1, "comment", item1, "user2Name",
                        LocalDateTime.of(2024, 11, 20, 11, 1, 1))));


        Collection<Booking> itemBookings = List.of(booking1, booking2);
        Collection<Comment> comments = List.of(comment);
        Collection<CommentDto> commentsDto = List.of(commentDto);

        when(bookingRepository.getBookingForItem(itemId, userId)).thenReturn(itemBookings);
        when(itemRepository.findById(itemId)).thenReturn(Optional.of(item1));
        when(itemRepository.getReferenceById(itemId)).thenReturn(item1);
        when(commentRepository.findByItem(item1)).thenReturn(comments);
        when(itemServiceAuxiliary.createBookingForItem(itemId, itemBookings, commentsDto)).thenReturn(itemDtoBooking);

        ItemDtoBooking returnedItemDtoBooking = itemService.getItemByIdAndUserId(userId, itemId);

        assertEquals(itemDtoBooking, returnedItemDtoBooking);
    }

    @Test
    void getItemByIdAndUserId_whenUserIsNotOwnerForItem_thenReturnWithoutLastAndNextBooking() {
        int userId = 2;
        int itemId = 1;
        User user1 = new User(1, "user1Name", "email1@mail.ru");
        User user2 = new User(2, "user2Name", "email2@mail.ru");
        Item item1 = new Item(1, "item1Name", "item1Description", true, user1, 0);


        CommentDto commentDto = new CommentDto(1, "comment", item1, "user2Name",
                LocalDateTime.of(2024, 11, 20, 11, 1, 1));
        Comment comment = new Comment(1, "comment", item1, user2,
                LocalDateTime.of(2024, 11, 20, 11, 1, 1));

        ItemDtoBooking itemDtoBooking = new ItemDtoBooking(1, "item1Name",
                "item1Description",
                true, 0,
                null,
                null,
                List.of(new CommentDto(1, "comment", item1, "user2Name",
                        LocalDateTime.of(2024, 11, 20, 11, 1, 1))));

        Collection<Comment> comments = List.of(comment);

        when(bookingRepository.getBookingForItem(itemId, userId)).thenReturn(new ArrayList<>());
        when(itemRepository.findById(itemId)).thenReturn(Optional.of(item1));
        when(itemRepository.getReferenceById(itemId)).thenReturn(item1);
        when(commentRepository.findByItem(item1)).thenReturn(comments);

        ItemDtoBooking returnedItemDtoBooking = itemService.getItemByIdAndUserId(userId, itemId);
        assertEquals(itemDtoBooking, returnedItemDtoBooking);
    }

    @Test
    void getItemsForUser() {
        int userId = 1;
        int itemId = 1;
        User user1 = new User(1, "user1Name", "email1@mail.ru");
        User user2 = new User(2, "user2Name", "email2@mail.ru");
        User user3 = new User(3, "user3Name", "email3@mail.ru");
        Item item1 = new Item(1, "item1Name", "item1Description", true, user1, 0);
        Comment comment = new Comment(1, "comment", item1, user2,
                LocalDateTime.of(2024, 11, 20, 11, 1, 1));

        Booking booking1 = new Booking(1, LocalDateTime.of(2024, 11, 20, 9, 1, 1),
                LocalDateTime.of(2024, 11, 20, 10, 1, 1),
                item1, user2, Status.APPROVED);
        Booking booking2 = new Booking(2, LocalDateTime.of(2024, 11, 21, 9, 1, 1),
                LocalDateTime.of(2024, 11, 21, 10, 1, 1),
                item1, user3, Status.WAITING);
        BookingDto lastBookingDto = new BookingDto(1, LocalDateTime.of(2024, 11, 20, 9, 1, 1),
                LocalDateTime.of(2024, 11, 20, 10, 1, 1),
                item1, user2, Status.APPROVED);
        BookingDto nextBookingDto = new BookingDto(2, LocalDateTime.of(2024, 11, 21, 9, 1, 1),
                LocalDateTime.of(2024, 11, 21, 10, 1, 1),
                item1, user3, Status.WAITING);

        Collection<Booking> itemBookings = List.of(booking1, booking2);
        CommentDto commentDto = new CommentDto(1, "comment", item1, "user2Name",
                LocalDateTime.of(2024, 11, 20, 11, 1, 1));

        ItemDtoBooking itemDtoBooking = new ItemDtoBooking(1, "item1Name",
                "item1Description",
                true, 0,
                lastBookingDto,
                nextBookingDto,
                List.of(new CommentDto(1, "comment", item1, "user2Name",
                        LocalDateTime.of(2024, 11, 20, 11, 1, 1))));

        doNothing().when(userService).checkUserExist(userId);
        when(userService.getUserById(userId)).thenReturn(user1);
        when(itemRepository.findByOwner(user1)).thenReturn(List.of(item1));
        when(commentRepository.findByItem(item1)).thenReturn(List.of(comment));
        when(bookingRepository.getBookingForItem(itemId, userId)).thenReturn(itemBookings);
        when(itemServiceAuxiliary.createBookingForItem(itemId, itemBookings, List.of(commentDto))).thenReturn(itemDtoBooking);

        Collection<ItemDtoBooking> returnedItemDtoBookings = itemService.getItemsForUser(userId);
        assertEquals(List.of(itemDtoBooking), returnedItemDtoBookings);
    }

    @Test
    void searchItems_whenTextIsEmpty_thenReturnEmptyList() {
        String text = "";
        assertEquals(new ArrayList<>(), itemService.searchItems(text));
        verify(itemRepository, never()).findByNameOrDescription(text.toLowerCase());
    }

    @Test
    void searchItems_whenTextExist_thenReturnItems() {
        String text = "description";
        User user1 = new User(1, "user1Name", "email1@mail.ru");
        Item item1 = new Item(1, "item1Name", "item1Description", true, user1, 0);
        when(itemRepository.findByNameOrDescription(text)).thenReturn(List.of(item1));

        assertEquals(List.of(item1), itemService.searchItems(text));
    }


    @Test
    void checkItemExist_whenItemDoNotExist_thenExceptionThrow() {
        int itemId = 1;
        when(itemRepository.findById(itemId)).thenReturn(Optional.empty());

        assertThrows(NotFoundException.class, () -> itemService.checkItemExist(itemId));
    }

    @Test
    void checkItemAvailable_whenItemNotAvailable_thenExceptionThrow() {
        int itemId = 1;
        User user = new User(1, "userName", "email@mail.ru");
        Item item = new Item(1, "itemName", "itemDescription", false, user, 0);

        when(itemRepository.findById(itemId)).thenReturn(Optional.of(new Item()));
        when(itemRepository.getReferenceById(itemId)).thenReturn(item);

        assertThrows(InternalServerErrorException.class, () -> itemService.checkItemAvailable(itemId));
    }

    @Test
    void addComment_whenUserDidNotHaveApprovedBookingInPast_thenReturnException() {
        int userId = 1;
        int itemId = 1;
        User user1 = new User(1, "user1Name", "email1@mail.ru");
        Item item1 = new Item(1, "item1Name", "item1Description", true, user1, 0);
        CommentCreateDto commentCreateDto = new CommentCreateDto("comment");

        doNothing().when(userService).checkUserExist(userId);
        when(itemRepository.findById(itemId)).thenReturn(Optional.of(item1));
        when(bookingRepository.getApprovedBookingForBookerPast(userId, itemId)).thenReturn(List.of());
        assertThrows(BadRequestException.class, () -> itemService.addComment(userId, itemId, commentCreateDto));
    }

    @Test
    void addComment() {
        int userId = 2;
        int itemId = 1;
        User user1 = new User(1, "user1Name", "email1@mail.ru");
        User user2 = new User(2, "user2Name", "email2@mail.ru");
        User user3 = new User(3, "user3Name", "email3@mail.ru");
        Item item1 = new Item(1, "item1Name", "item1Description", true, user1, 0);
        Booking booking1 = new Booking(1, LocalDateTime.of(2024, 11, 20, 9, 1, 1),
                LocalDateTime.of(2024, 11, 20, 10, 1, 1),
                item1, user2, Status.APPROVED);
        CommentCreateDto commentCreateDto = new CommentCreateDto("comment");
        Comment comment = new Comment("comment", item1, user2,
                LocalDateTime.now().withNano(0));

        CommentDto commentDto = new CommentDto(0, "comment", item1, "user2Name",
                LocalDateTime.now().withNano(0));


        doNothing().when(userService).checkUserExist(userId);
        when(itemRepository.findById(itemId)).thenReturn(Optional.of(item1));
        when(bookingRepository.getApprovedBookingForBookerPast(userId, itemId)).thenReturn(List.of(booking1));
        when(itemRepository.getReferenceById(itemId)).thenReturn(item1);
        when(userService.getUserById(userId)).thenReturn(user2);

        CommentDto retuurnedCommentDto = itemService.addComment(userId, itemId, commentCreateDto);
        assertEquals(commentDto, retuurnedCommentDto);
        verify(commentRepository, times(1)).save(comment);
    }

    @Test
    void checkItemForUser_whenUserOwner_thenReturnUser() {
        int userId = 1;
        int itemId = 1;
        User user = new User(1, "userName", "email@mail.ru");
        Item item = new Item(0, "itemName", "itemDescription", true, user, 0);

        when(itemRepository.getItemForUser(userId, itemId)).thenReturn(item);
        Item retirnedItem = itemService.checkItemForUser(userId, itemId);
        assertEquals(item, retirnedItem);
    }

    @Test
    void checkItemForUser_whenUserIsNotOwner_thenExceptionThrow() {
        int userId = 1;
        int itemId = 1;
        User user = new User(1, "userName", "email@mail.ru");
        Item item = new Item(0, "itemName", "itemDescription", true, user, 0);

        when(itemRepository.getItemForUser(userId, itemId)).thenReturn(null);
        assertThrows(NotFoundException.class, () -> itemService.checkItemForUser(userId, itemId));
    }

}