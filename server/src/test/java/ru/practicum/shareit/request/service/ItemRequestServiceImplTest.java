package ru.practicum.shareit.request.service;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import ru.practicum.shareit.item.dto.ItemDtoRequest;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.item.repository.ItemRepository;
import ru.practicum.shareit.request.dto.ItemRequestCreateDto;
import ru.practicum.shareit.request.dto.ItemRequestDtoWithAnswer;
import ru.practicum.shareit.request.model.ItemRequest;
import ru.practicum.shareit.request.repository.ItemRequestRepository;
import ru.practicum.shareit.user.User;
import ru.practicum.shareit.user.repository.UserRepository;
import ru.practicum.shareit.user.service.UserService;

import java.time.LocalDateTime;
import java.util.Collection;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class ItemRequestServiceImplTest {
    @Mock
    UserService userService;
    @Mock
    ItemRequestRepository itemRequestRepository;
    @Mock
    ItemRepository itemRepository;
    @InjectMocks
    ItemRequestServiceImpl itemRequestService;

    @Test
    void addRequest() {
        int userId = 1;
        User user1 = new User(1, "user1Name", "email1@mail.ru");
        ItemRequestCreateDto itemRequestCreateDto =
                new ItemRequestCreateDto("request description");
        ItemRequest itemRequest = new ItemRequest(0, "request description", user1,
                LocalDateTime.now().withNano(0));

        doNothing().when(userService).checkUserExist(userId);
        when(userService.getUserById(userId)).thenReturn(user1);
        when(itemRequestRepository.save(itemRequest)).thenReturn(itemRequest);

        ItemRequest returnedRequest = itemRequestService.addRequest(userId, itemRequestCreateDto);
        assertEquals(itemRequest, returnedRequest);
    }

    @Test
    void getRequests() {
        int userId = 1;
        int itemId = 1;
        User user1 = new User(1, "user1Name", "email1@mail.ru");
        Item item1 = new Item(1, "itemRequestName", "item1Description", true, user1, 1);

        ItemDtoRequest itemDtoRequest = new ItemDtoRequest(1, "itemRequestName", 1);
        ItemRequest itemRequest = new ItemRequest(1, "request description", user1,
                LocalDateTime.now().withNano(0));
        ItemRequestDtoWithAnswer itemRequestDtoWithAnswer = new ItemRequestDtoWithAnswer(
                1, "request description",
                LocalDateTime.now().withNano(0),
                List.of(itemDtoRequest));
        doNothing().when(userService).checkUserExist(userId);
        when(itemRequestRepository.getRequestsForUser(userId)).thenReturn(List.of(itemRequest));
        when(itemRepository.findByRequest(itemId)).thenReturn(List.of(item1));

        Collection<ItemRequestDtoWithAnswer> returnedItemRequestDtoWithAnswers = itemRequestService.getRequests(userId);
        assertEquals(List.of(itemRequestDtoWithAnswer), returnedItemRequestDtoWithAnswers);
    }

    @Test
    void getRequestsForAllUsers() {
        User user1 = new User(1, "user1Name", "email1@mail.ru");
        ItemRequest itemRequest = new ItemRequest(1, "request description", user1,
                LocalDateTime.now().withNano(0));
        when(itemRequestRepository.findAll()).thenReturn(List.of(itemRequest));
        Collection<ItemRequest> returnedItemRequests = itemRequestService.getRequestsForAllUsers();
        assertEquals(List.of(itemRequest), returnedItemRequests);
    }

    @Test
    void getRequestData() {
        int itemId = 1;
        int requestId = 1;
        User user1 = new User(1, "user1Name", "email1@mail.ru");
        Item item1 = new Item(1, "itemRequestName", "item1Description", true, user1, 1);
        ItemDtoRequest itemDtoRequest = new ItemDtoRequest(1, "itemRequestName", 1);

        ItemRequest itemRequest = new ItemRequest(1, "request description", user1,
                LocalDateTime.now().withNano(0));
        ItemRequestDtoWithAnswer itemRequestDtoWithAnswer = new ItemRequestDtoWithAnswer(
                1, "request description",
                LocalDateTime.now().withNano(0),
                List.of(itemDtoRequest));

        when(itemRequestRepository.getReferenceById(requestId)).thenReturn(itemRequest);
        when(itemRepository.findByRequest(itemId)).thenReturn(List.of(item1));

        ItemRequestDtoWithAnswer returnedItemRequestDtoWithAnswer = itemRequestService.getRequestData(requestId);
        assertEquals(itemRequestDtoWithAnswer, returnedItemRequestDtoWithAnswer);

    }
}