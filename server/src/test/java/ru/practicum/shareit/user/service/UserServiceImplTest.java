package ru.practicum.shareit.user.service;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import ru.practicum.shareit.exception.ConflictException;
import ru.practicum.shareit.exception.NotFoundException;
import ru.practicum.shareit.user.User;
import ru.practicum.shareit.user.dto.UserCreateDto;
import ru.practicum.shareit.user.repository.UserRepository;

import java.util.Collection;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class UserServiceImplTest {
    @Mock
    UserRepository userRepository;
    @InjectMocks
    UserServiceImpl userService;

    @Test
    void saveUser_whenEmailUnique_thenSave() {
        UserCreateDto userCreateDto = new UserCreateDto("userName", "email@mail.ru");
        User user = new User("userName", "email@mail.ru");

        when(userRepository.findByEmail(user.getEmail())).thenReturn(null);
        when(userRepository.save(user)).thenReturn(user);

        User returnedUser = userService.saveUser(userCreateDto);
        assertEquals(user, returnedUser);
    }

    @Test
    void saveUser_whenEmailNotUnique_thenConflictExceptionThrown() {
        UserCreateDto userCreateDto = new UserCreateDto("userName", "email@mail.ru");
        User user = new User(1, "userName", "email@mail.ru");

        when(userRepository.findByEmail(user.getEmail())).thenReturn(user);
        assertThrows(ConflictException.class, () -> userService.saveUser(userCreateDto));

    }

    @Test
    void updateUser_whenEmailUnique_thenUpdate() {
        UserCreateDto userCreateDto = new UserCreateDto("userNameUpdated", "emailUpdated@mail.ru");
        User user = new User(1, "userName", "email@mail.ru");
        User updatedUser = new User(1, "userNameUpdated", "emailUpdated@mail.ru");

        when(userService.getUserById(1)).thenReturn(user);
        when(userRepository.findByEmail(updatedUser.getEmail())).thenReturn(null);
        when(userRepository.save(updatedUser)).thenReturn(updatedUser);
        User returnedUpdatedUser = userService.updateUser(user.getId(), userCreateDto);
        assertEquals(updatedUser, returnedUpdatedUser);
        verify(userRepository, times(1)).save(user);
    }

    @Test
    void updateUser_whenEmailNotUnique_thenExceptionThrown() {
        UserCreateDto userCreateDto = new UserCreateDto("userNameUpdated", "emailUpdated@mail.ru");
        User user = new User(1, "userName", "email@mail.ru");
        User user2 = new User(2, "user2Name", "email2@mail.ru");
        User updatedUser = new User(1, "userNameUpdated", "emailUpdated@mail.ru");

        when(userService.getUserById(1)).thenReturn(user);
        when(userRepository.findByEmail(updatedUser.getEmail())).thenReturn(user2);
        assertThrows(ConflictException.class, () -> userService.updateUser(1, userCreateDto));
        verify(userRepository, never()).save(user);
    }

    @Test
    void getAllUsers() {
        User user1 = new User(1, "user1Name", "email1@mail.ru");
        User user2 = new User(2, "user2Name", "email2@mail.ru");
        List<User> users = List.of(user1, user2);

        when(userRepository.findAll()).thenReturn(users);

        Collection<User> returnedUsers = userService.getAllUsers();
        assertEquals(users, returnedUsers);
    }

    @Test
    void getUserById() {
        int userId = 1;
        User user = new User(1, "userName", "email@mail.ru");

        when(userRepository.getReferenceById(userId)).thenReturn(user);
        User returnedUser = userService.getUserById(userId);

        assertEquals(user, returnedUser);
    }

    @Test
    void deleteUserById() {
        int userId = 1;
        User user = new User(1, "userName", "email@mail.ru");
        when(userService.getUserById(userId)).thenReturn(user);
        userService.deleteUserById(userId);
        verify(userRepository, times(1)).delete(user);
    }

    @Test
    void checkUserExist_whenUserExist() {
        int userId = 1;
        User user = new User(1, "userName", "email@mail.ru");

        when(userRepository.findById(userId)).thenReturn(Optional.of(user));
        userService.checkUserExist(userId);
        verify(userRepository, times(1)).findById(userId);


    }

    @Test
    void checkUserExist_whenUserDoNotExist_thenThrowException() {
        int userId = 1;
        when(userRepository.findById(userId)).thenReturn(Optional.empty());

        assertThrows(NotFoundException.class, () -> userService.checkUserExist(userId));
    }
}