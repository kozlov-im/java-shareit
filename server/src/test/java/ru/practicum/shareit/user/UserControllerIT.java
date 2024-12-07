package ru.practicum.shareit.user;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import ru.practicum.shareit.user.dto.UserCreateDto;
import ru.practicum.shareit.user.dto.UserUpdateDto;
import ru.practicum.shareit.user.service.UserService;

import java.nio.charset.StandardCharsets;
import java.util.Collection;
import java.util.List;

import static org.hamcrest.Matchers.is;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;


@WebMvcTest(controllers = UserController.class)
class UserControllerIT {
    @Autowired
    ObjectMapper mapper;
    @MockBean
    UserService userService;
    @Autowired
    MockMvc mvc;
    UserCreateDto userCreateDto;
    UserUpdateDto userUpdateDto;
    User user;

    @BeforeEach
    void setUp() {
        userCreateDto = new UserCreateDto("userName", "email@mail.ru");
        userUpdateDto = new UserUpdateDto("userName", "email@mail.ru");
        user = new User(1, "userName", "email@mail.ru");
    }

    @Test
    void createUser() throws Exception {
        when(userService.saveUser(userCreateDto)).thenReturn(user);
        mvc.perform(post("/users")
                        .content(mapper.writeValueAsString(userCreateDto))
                        .characterEncoding(StandardCharsets.UTF_8)
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id", is(user.getId())))
                .andExpect(jsonPath("$.name", is(user.getName())))
                .andExpect(jsonPath("$.email", is(user.getEmail())));
    }

    @Test
    void updateUser() throws Exception {
        int userId = 1;
        User updatedUser = new User(1, "userNameUpdated", "emailUpdated@mail.ru");
        when(userService.updateUser(userId, userUpdateDto)).thenReturn(updatedUser);
        mvc.perform(patch("/users/{userId}", userId)
                        .content(mapper.writeValueAsString(userCreateDto))
                        .characterEncoding(StandardCharsets.UTF_8)
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id", is(updatedUser.getId())))
                .andExpect(jsonPath("$.name", is(updatedUser.getName())))
                .andExpect(jsonPath("$.email", is(updatedUser.getEmail())));
    }

    @Test
    void getAllUsers() throws Exception {
        User user2 = new User(2, "user2Name", "email2@mail.ru");
        Collection<User> users = List.of(user, user2);
        when(userService.getAllUsers()).thenReturn(users);
        mvc.perform(get("/users")
                        .content(mapper.writeValueAsString(users))
                        .characterEncoding(StandardCharsets.UTF_8)
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.[0].id", is(user.getId())))
                .andExpect(jsonPath("$.[1].id", is(user2.getId())))
                .andExpect(jsonPath("$.[0].name", is(user.getName())))
                .andExpect(jsonPath("$.[1].name", is(user2.getName())))
                .andExpect(jsonPath("$.[0].email", is(user.getEmail())))
                .andExpect(jsonPath("$.[1].email", is(user2.getEmail())));
    }

    @Test
    void getUserById() throws Exception {
        int userId = 1;
        when(userService.getUserById(userId)).thenReturn(user);
        mvc.perform(get("/users/{userId}", userId)
                        .content(mapper.writeValueAsString(user))
                        .characterEncoding(StandardCharsets.UTF_8)
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id", is(user.getId())))
                .andExpect(jsonPath("$.name", is(user.getName())))
                .andExpect(jsonPath("$.email", is(user.getEmail())));
    }

    @Test
    void deleteUserById() {
    }
}