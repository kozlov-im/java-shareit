package ru.practicum.shareit.request;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import ru.practicum.shareit.item.dto.ItemDtoRequest;
import ru.practicum.shareit.request.dto.ItemRequestCreateDto;
import ru.practicum.shareit.request.dto.ItemRequestDtoWithAnswer;
import ru.practicum.shareit.request.model.ItemRequest;
import ru.practicum.shareit.request.service.ItemRequestService;
import ru.practicum.shareit.user.User;

import java.nio.charset.StandardCharsets;
import java.time.LocalDateTime;
import java.util.List;

import static org.hamcrest.Matchers.is;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(controllers = ItemRequestController.class)
class ItemRequestControllerIT {

    @Autowired
    ObjectMapper mapper;
    @MockBean
    ItemRequestService itemRequestService;
    @Autowired
    MockMvc mvc;

    @Test
    void addRequest() throws Exception {
        int userId = 1;
        User user1 = new User(1, "user1Name", "email1@mail.ru");
        ItemRequestCreateDto itemRequestCreateDto =
                new ItemRequestCreateDto("request description");
        ItemRequest itemRequest = new ItemRequest(1, "request description", user1,
                LocalDateTime.now().withNano(0));
        when(itemRequestService.addRequest(userId, itemRequestCreateDto)).thenReturn(itemRequest);

        mvc.perform(post("/requests")
                        .content(mapper.writeValueAsString(itemRequestCreateDto))
                        .header("X-Sharer-User-Id", userId)
                        .characterEncoding(StandardCharsets.UTF_8)
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id", is(itemRequest.getId())))
                .andExpect(jsonPath("$.description", is(itemRequest.getDescription())))
                .andExpect(jsonPath("$.user.id", is(itemRequest.getUser().getId())))
                .andExpect(jsonPath("$.user.name", is(itemRequest.getUser().getName())))
                .andExpect(jsonPath("$.user.email", is(itemRequest.getUser().getEmail())))
                .andExpect(jsonPath("$.created", is(itemRequest.getCreated().toString())));
    }

    @Test
    void getRequests() throws Exception {
        int userId = 1;
        ItemDtoRequest itemDtoRequest = new ItemDtoRequest(1, "itemRequestName", 2);
        ItemRequestDtoWithAnswer itemRequestDtoWithAnswer = new ItemRequestDtoWithAnswer(
                1, "request description",
                LocalDateTime.of(2024, 11, 20, 9, 1, 1),
                List.of(itemDtoRequest));

        when(itemRequestService.getRequests(userId)).thenReturn(List.of(itemRequestDtoWithAnswer));

        mvc.perform(get("/requests")
                        .header("X-Sharer-User-Id", userId)
                        .characterEncoding(StandardCharsets.UTF_8)
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.[0].id", is(itemRequestDtoWithAnswer.getId())))
                .andExpect(jsonPath("$.[0].description", is(itemRequestDtoWithAnswer.getDescription())))
                .andExpect(jsonPath("$.[0].created", is(itemRequestDtoWithAnswer.getCreated().toString())));
    }

    @Test
    void getRequestsForAllUsers() throws Exception {
        User user1 = new User(1, "user1Name", "email1@mail.ru");
        ItemRequest itemRequest = new ItemRequest(1, "request description", user1,
                LocalDateTime.now().withNano(0));
        when(itemRequestService.getRequestsForAllUsers()).thenReturn(List.of(itemRequest));

        mvc.perform(get("/requests/all")
                        .characterEncoding(StandardCharsets.UTF_8)
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.[0].id", is(itemRequest.getId())))
                .andExpect(jsonPath("$.[0].description", is(itemRequest.getDescription())))
                .andExpect(jsonPath("$.[0].user.id", is(itemRequest.getUser().getId())))
                .andExpect(jsonPath("$.[0].user.name", is(itemRequest.getUser().getName())))
                .andExpect(jsonPath("$.[0].user.email", is(itemRequest.getUser().getEmail())))
                .andExpect(jsonPath("$.[0].created", is(itemRequest.getCreated().toString())));
    }

    @Test
    void getRequestData() throws Exception {
        int userId = 1;
        int requestId = 1;
        ItemDtoRequest itemDtoRequest = new ItemDtoRequest(1, "itemRequestName", 2);
        ItemRequestDtoWithAnswer itemRequestDtoWithAnswer = new ItemRequestDtoWithAnswer(
                1, "request description",
                LocalDateTime.of(2024, 11, 20, 9, 1, 1),
                List.of(itemDtoRequest));

        when(itemRequestService.getRequestData(requestId)).thenReturn(itemRequestDtoWithAnswer);

        mvc.perform(get("/requests/{requestId}", requestId)
                        .characterEncoding(StandardCharsets.UTF_8)
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id", is(itemRequestDtoWithAnswer.getId())))
                .andExpect(jsonPath("$.description", is(itemRequestDtoWithAnswer.getDescription())))
                .andExpect(jsonPath("$.created", is(itemRequestDtoWithAnswer.getCreated().toString())));

    }
}