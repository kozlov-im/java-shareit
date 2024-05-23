package ru.practicum.shareit.user.service;

import ru.practicum.shareit.user.User;

import java.util.Collection;

public interface UserService {

    User saveUser(User user);

    Collection<User> getAllUsers();

    User getUserById(int userId);

    User updateUser(int userId, User user);

    void deleteUserById(int userId);
}
