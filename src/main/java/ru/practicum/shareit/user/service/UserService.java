package ru.practicum.shareit.user.service;

import ru.practicum.shareit.user.User;
import ru.practicum.shareit.user.dto.UserDto;

import java.util.Collection;

public interface UserService {
    User saveUser(UserDto userDto);

    User updateUser(int userId, UserDto userDto);

    Collection<User> getAllUsers();

    User getUserById(int userId);

    void deleteUserById(int userId);
}
