package ru.practicum.shareit.user;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import ru.practicum.shareit.user.dto.UserCreateDto;
import ru.practicum.shareit.user.dto.UserDto;
import ru.practicum.shareit.user.dto.UserUpdateDto;
import ru.practicum.shareit.user.service.UserService;

import java.util.Collection;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class UserControllerTest {
    @Mock
    UserService userService;
    @InjectMocks
    UserController userController;
    UserCreateDto userCreateDto;
    UserUpdateDto userUpdateDto;
    User user;
    UserDto userDto;

    @BeforeEach
    void setUp() {
        userCreateDto = new UserCreateDto("userName", "email@mail.ru");
        userUpdateDto = new UserUpdateDto("userName", "email@mail.ru");
        user = new User(1, "userName", "email@mail.ru");
        userDto = new UserDto(1, "userName", "email@mail.ru");
    }

    @Test
    void createUser() {
        when(userService.saveUser(userCreateDto)).thenReturn(user);
        UserDto returnedUser = userController.createUser(userCreateDto);
        assertEquals(userDto, returnedUser);
    }

    @Test
    void updateUser() {
        User updatedUser = new User(1, "userNameUpdated", "emailUpdated@mail.ru");
        UserDto updatedUserDto = new UserDto(1, "userNameUpdated", "emailUpdated@mail.ru");
        int userId = 1;

        when(userService.updateUser(userId, userUpdateDto)).thenReturn(updatedUser);
        UserDto returnedUser = userController.updateUser(userUpdateDto, userId);
        assertEquals(updatedUserDto, returnedUser);
    }

    @Test
    void getAllUsers() {
        User user2 = new User(2, "user2Name", "email2@mail.ru");
        Collection<User> users = List.of(user, user2);
        UserDto userDto2 = new UserDto(2, "user2Name", "email2@mail.ru");
        Collection<UserDto> usersDto = List.of(userDto, userDto2);

        when(userService.getAllUsers()).thenReturn(users);
        Collection<UserDto> returnedUsersDto = userController.getAllUsers();
        assertEquals(usersDto, returnedUsersDto);
    }

    @Test
    void getUserById() {
        int userId = 1;

        when(userService.getUserById(userId)).thenReturn(user);
        UserDto returnedUserDto = userController.getUserById(userId);
        assertEquals(userDto, returnedUserDto);
    }

    @Test
    void deleteUserById() {
        userController.deleteUserById(1);
        verify(userService, times(1)).deleteUserById(1);
    }
}