package ru.practicum.shareit.user.service;

import ru.practicum.shareit.user.User;
import ru.practicum.shareit.user.dto.UserCreateDto;

import java.util.Collection;

public interface UserService {
    User saveUser(UserCreateDto userCreateDto);

    User updateUser(int userId, UserCreateDto userCreateDto);

    Collection<User> getAllUsers();

    User getUserById(int userId);

    void deleteUserById(int userId);
}
