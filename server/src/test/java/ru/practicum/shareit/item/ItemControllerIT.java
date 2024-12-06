package ru.practicum.shareit.item;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import ru.practicum.shareit.booking.Status;
import ru.practicum.shareit.booking.dto.BookingDto;
import ru.practicum.shareit.item.dto.*;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.item.service.ItemService;
import ru.practicum.shareit.user.User;

import java.nio.charset.StandardCharsets;
import java.time.LocalDateTime;
import java.util.Collection;
import java.util.List;

import static org.hamcrest.Matchers.is;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(controllers = ItemController.class)
class ItemControllerIT {

    @Autowired
    ObjectMapper mapper;

    @MockBean
    ItemService itemService;
    @Autowired
    MockMvc mvc;
    ItemCreateDto itemCreateDto;
    ItemDto itemDto;
    User user;
    Item item;


    @BeforeEach
    void setUp() {
        user = new User(1, "userName", "email@mail.ru");
        itemCreateDto = new ItemCreateDto("itemName", "itemDescription", true, user, 0);
        item = new Item(1, "itemName", "itemDescription", true, user, 0);
    }

    @Test
    void createItem() throws Exception {
        int userId = 1;
        when(itemService.createItem(userId, itemCreateDto)).thenReturn(item);

        mvc.perform(post("/items")
                        .content(mapper.writeValueAsString(itemCreateDto))
                        .header("X-Sharer-User-Id", userId)
                        .characterEncoding(StandardCharsets.UTF_8)
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id", is(item.getId())))
                .andExpect(jsonPath("$.name", is(item.getName())))
                .andExpect(jsonPath("$.description", is(item.getDescription())))
                .andExpect(jsonPath("$.available", is(item.getAvailable())))
                .andExpect(jsonPath("$.name", is(item.getName())))
                .andExpect(jsonPath("$.owner.id", is(item.getOwner().getId())))
                .andExpect(jsonPath("$.owner.name", is(item.getOwner().getName())))
                .andExpect(jsonPath("$.owner.email", is(item.getOwner().getEmail())))
                .andExpect(jsonPath("$.requestId", is(item.getRequest())));
    }

    @Test
    void updateItem() throws Exception {
        int userId = 1;
        int itemId = 1;
        ItemCreateDto itemUpdateDto = new ItemCreateDto("itemNameUpdated", "itemDescriptionUpdated", true, user, 0);
        when(itemService.updateItem(userId, itemId, itemUpdateDto)).thenReturn(item);

        mvc.perform(patch("/items/{itemId}", itemId)
                        .content(mapper.writeValueAsString(itemUpdateDto))
                        .header("X-Sharer-User-Id", userId)
                        .characterEncoding(StandardCharsets.UTF_8)
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id", is(item.getId())))
                .andExpect(jsonPath("$.name", is(item.getName())))
                .andExpect(jsonPath("$.description", is(item.getDescription())))
                .andExpect(jsonPath("$.available", is(item.getAvailable())))
                .andExpect(jsonPath("$.name", is(item.getName())))
                .andExpect(jsonPath("$.owner.id", is(item.getOwner().getId())))
                .andExpect(jsonPath("$.owner.name", is(item.getOwner().getName())))
                .andExpect(jsonPath("$.owner.email", is(item.getOwner().getEmail())))
                .andExpect(jsonPath("$.requestId", is(item.getRequest())));
    }

    @Test
    void getItemByIdAndUserId() throws Exception {
        int userId = 1;
        int itemId = 1;
        User user1 = new User(1, "user1Name", "email1@mail.ru");
        User user2 = new User(2, "user2Name", "email2@mail.ru");
        User user3 = new User(3, "user3Name", "email3@mail.ru");
        Item item1 = new Item(1, "item1Name", "item1Description", true, user1, 0);

        BookingDto lastBookingDto = new BookingDto(1, LocalDateTime.of(2024, 11, 20, 9, 1, 1),
                LocalDateTime.of(2024, 11, 20, 10, 1, 1),
                item1, user2, Status.APPROVED);
        BookingDto nextBookingDto = new BookingDto(2, LocalDateTime.of(2024, 11, 21, 9, 1, 1),
                LocalDateTime.of(2024, 11, 21, 10, 1, 1),
                item1, user3, Status.WAITING);
        ItemDtoBooking itemDtoBooking = new ItemDtoBooking(1, "item1Name",
                "item1Description",
                true, 0,
                lastBookingDto,
                nextBookingDto,
                List.of(new CommentDto(1, "comment", item1, "user2Name",
                        LocalDateTime.of(2024, 11, 20, 11, 1, 1))));

        when(itemService.getItemByIdAndUserId(userId, itemId)).thenReturn(itemDtoBooking);

        mvc.perform(get("/items/{itemId}", itemId)
                        .content(mapper.writeValueAsString(itemDtoBooking))
                        .header("X-Sharer-User-Id", userId)
                        .characterEncoding(StandardCharsets.UTF_8)
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id", is(itemDtoBooking.getId())))
                .andExpect(jsonPath("$.name", is(itemDtoBooking.getName())))
                .andExpect(jsonPath("$.description", is(itemDtoBooking.getDescription())))
                .andExpect(jsonPath("$.available", is(itemDtoBooking.getAvailable())))
                .andExpect(jsonPath("$.lastBooking.id", is(itemDtoBooking.getLastBooking().getId())))
                .andExpect(jsonPath("$.lastBooking.start", is(itemDtoBooking.getLastBooking().getStart().toString())))
                .andExpect(jsonPath("$.lastBooking.end", is(itemDtoBooking.getLastBooking().getEnd().toString())))
                .andExpect(jsonPath("$.lastBooking.item.id", is(itemDtoBooking.getLastBooking().getItem().getId())))
                .andExpect(jsonPath("$.lastBooking.item.name", is(itemDtoBooking.getLastBooking().getItem().getName())))
                .andExpect(jsonPath("$.lastBooking.item.description", is(itemDtoBooking.getLastBooking().getItem().getDescription())))
                .andExpect(jsonPath("$.lastBooking.item.available", is(itemDtoBooking.getLastBooking().getItem().getAvailable())))
                .andExpect(jsonPath("$.lastBooking.item.owner.id", is(itemDtoBooking.getLastBooking().getItem().getOwner().getId())))
                .andExpect(jsonPath("$.lastBooking.item.owner.name", is(itemDtoBooking.getLastBooking().getItem().getOwner().getName())))
                .andExpect(jsonPath("$.lastBooking.item.owner.email", is(itemDtoBooking.getLastBooking().getItem().getOwner().getEmail())))
                .andExpect(jsonPath("$.nextBooking.id", is(itemDtoBooking.getNextBooking().getId())))
                .andExpect(jsonPath("$.nextBooking.start", is(itemDtoBooking.getNextBooking().getStart().toString())))
                .andExpect(jsonPath("$.nextBooking.end", is(itemDtoBooking.getNextBooking().getEnd().toString())))
                .andExpect(jsonPath("$.nextBooking.item.id", is(itemDtoBooking.getNextBooking().getItem().getId())))
                .andExpect(jsonPath("$.nextBooking.item.name", is(itemDtoBooking.getNextBooking().getItem().getName())))
                .andExpect(jsonPath("$.nextBooking.item.description", is(itemDtoBooking.getNextBooking().getItem().getDescription())))
                .andExpect(jsonPath("$.nextBooking.item.available", is(itemDtoBooking.getNextBooking().getItem().getAvailable())))
                .andExpect(jsonPath("$.nextBooking.item.owner.id", is(itemDtoBooking.getNextBooking().getItem().getOwner().getId())))
                .andExpect(jsonPath("$.nextBooking.item.owner.name", is(itemDtoBooking.getNextBooking().getItem().getOwner().getName())))
                .andExpect(jsonPath("$.nextBooking.item.owner.email", is(itemDtoBooking.getNextBooking().getItem().getOwner().getEmail())));
    }

    @Test
    void getItemsForUser() throws Exception {
        int userId = 1;
        User user1 = new User(1, "user1Name", "email1@mail.ru");
        User user2 = new User(2, "user2Name", "email2@mail.ru");
        User user3 = new User(3, "user3Name", "email3@mail.ru");
        Item item1 = new Item(1, "item1Name", "item1Description", true, user1, 0);

        BookingDto lastBookingDto = new BookingDto(1, LocalDateTime.of(2024, 11, 20, 9, 1, 1),
                LocalDateTime.of(2024, 11, 20, 10, 1, 1),
                item1, user2, Status.APPROVED);
        BookingDto nextBookingDto = new BookingDto(2, LocalDateTime.of(2024, 11, 21, 9, 1, 1),
                LocalDateTime.of(2024, 11, 21, 10, 1, 1),
                item1, user3, Status.WAITING);
        ItemDtoBooking itemDtoBooking = new ItemDtoBooking(1, "item1Name",
                "item1Description",
                true, 0,
                lastBookingDto,
                nextBookingDto,
                List.of(new CommentDto(1, "comment", item1, "user2Name",
                        LocalDateTime.of(2024, 11, 20, 11, 1, 1))));

        Collection<ItemDtoBooking> itemDtoBookings = List.of(itemDtoBooking);
        when(itemService.getItemsForUser(userId)).thenReturn(itemDtoBookings);

        mvc.perform(get("/items")
                        .content(mapper.writeValueAsString(itemDtoBooking))
                        .header("X-Sharer-User-Id", userId)
                        .characterEncoding(StandardCharsets.UTF_8)
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.[0].id", is(itemDtoBooking.getId())))
                .andExpect(jsonPath("$.[0].name", is(itemDtoBooking.getName())))
                .andExpect(jsonPath("$.[0].description", is(itemDtoBooking.getDescription())))
                .andExpect(jsonPath("$.[0].available", is(itemDtoBooking.getAvailable())))
                .andExpect(jsonPath("$.[0].lastBooking.id", is(itemDtoBooking.getLastBooking().getId())))
                .andExpect(jsonPath("$.[0].lastBooking.start", is(itemDtoBooking.getLastBooking().getStart().toString())))
                .andExpect(jsonPath("$.[0].lastBooking.end", is(itemDtoBooking.getLastBooking().getEnd().toString())))
                .andExpect(jsonPath("$.[0].lastBooking.item.id", is(itemDtoBooking.getLastBooking().getItem().getId())))
                .andExpect(jsonPath("$.[0].lastBooking.item.name", is(itemDtoBooking.getLastBooking().getItem().getName())))
                .andExpect(jsonPath("$.[0].lastBooking.item.description", is(itemDtoBooking.getLastBooking().getItem().getDescription())))
                .andExpect(jsonPath("$.[0].lastBooking.item.available", is(itemDtoBooking.getLastBooking().getItem().getAvailable())))
                .andExpect(jsonPath("$.[0].lastBooking.item.owner.id", is(itemDtoBooking.getLastBooking().getItem().getOwner().getId())))
                .andExpect(jsonPath("$.[0].lastBooking.item.owner.name", is(itemDtoBooking.getLastBooking().getItem().getOwner().getName())))
                .andExpect(jsonPath("$.[0].lastBooking.item.owner.email", is(itemDtoBooking.getLastBooking().getItem().getOwner().getEmail())))
                .andExpect(jsonPath("$.[0].nextBooking.id", is(itemDtoBooking.getNextBooking().getId())))
                .andExpect(jsonPath("$.[0].nextBooking.start", is(itemDtoBooking.getNextBooking().getStart().toString())))
                .andExpect(jsonPath("$.[0].nextBooking.end", is(itemDtoBooking.getNextBooking().getEnd().toString())))
                .andExpect(jsonPath("$.[0].nextBooking.item.id", is(itemDtoBooking.getNextBooking().getItem().getId())))
                .andExpect(jsonPath("$.[0].nextBooking.item.name", is(itemDtoBooking.getNextBooking().getItem().getName())))
                .andExpect(jsonPath("$.[0].nextBooking.item.description", is(itemDtoBooking.getNextBooking().getItem().getDescription())))
                .andExpect(jsonPath("$.[0].nextBooking.item.available", is(itemDtoBooking.getNextBooking().getItem().getAvailable())))
                .andExpect(jsonPath("$.[0].nextBooking.item.owner.id", is(itemDtoBooking.getNextBooking().getItem().getOwner().getId())))
                .andExpect(jsonPath("$.[0].nextBooking.item.owner.name", is(itemDtoBooking.getNextBooking().getItem().getOwner().getName())))
                .andExpect(jsonPath("$.[0].nextBooking.item.owner.email", is(itemDtoBooking.getNextBooking().getItem().getOwner().getEmail())));
    }

    @Test
    void searchItems() throws Exception {
        String search = "searchString";
        User user1 = new User(1, "user1Name", "email1@mail.ru");
        Item item1 = new Item(1, "item1Name", "item1Description", true, user1, 0);
        itemDto = new ItemDto(1, "item1Name", "item1Description", true, user1, 0);
        Collection<Item> items = List.of(item1);

        when(itemService.searchItems(search)).thenReturn(items);

        mvc.perform(get("/items/search")
                        .content(mapper.writeValueAsString(items))
                        .param("text",  search)
                        .characterEncoding(StandardCharsets.UTF_8)
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.[0].id", is(item1.getId())))
                .andExpect(jsonPath("$.[0].name", is(item1.getName())))
                .andExpect(jsonPath("$.[0].description", is(item1.getDescription())))
                .andExpect(jsonPath("$.[0].available", is(item1.getAvailable())))
                .andExpect(jsonPath("$.[0].name", is(item1.getName())))
                .andExpect(jsonPath("$.[0].owner.id", is(item1.getOwner().getId())))
                .andExpect(jsonPath("$.[0].owner.name", is(item1.getOwner().getName())))
                .andExpect(jsonPath("$.[0].owner.email", is(item1.getOwner().getEmail())))
                .andExpect(jsonPath("$.[0].requestId", is(item1.getRequest())));
    }

    @Test
    void addComment() throws Exception {
        int userId = 1;
        int itemId = 1;
        User user1 = new User(1, "user1Name", "email1@mail.ru");
        Item item1 = new Item(1, "item1Name", "item1Description", true, user1, 0);
        CommentCreateDto commentCreateDto = new CommentCreateDto("comment");
        CommentDto commentDto = new CommentDto(1, "comment", item1, "authorName",
                LocalDateTime.of(2024, 11, 21, 9, 1, 1));

        when(itemService.addComment(userId, itemId, commentCreateDto)).thenReturn(commentDto);

        mvc.perform(post("/items/{itemId}/comment", itemId)
                        .content(mapper.writeValueAsString(commentDto))
                        .header("X-Sharer-User-Id", userId)
                        .characterEncoding(StandardCharsets.UTF_8)
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id", is(commentDto.getId())))
                .andExpect(jsonPath("$.text", is(commentDto.getText())))
                .andExpect(jsonPath("$.item.id", is(commentDto.getItem().getId())));
    }
}