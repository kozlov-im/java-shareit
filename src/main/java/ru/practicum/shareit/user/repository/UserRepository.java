package ru.practicum.shareit.user.repository;

import ru.practicum.shareit.user.User;

import java.util.Collection;

public interface UserRepository {
    User saveUser(User user);

    Collection<User> getAllUsers();

    User getUserById(int userId);

    void updateUserName(int userId, User user);

    void updateUserEmail(int userId, User user);

    void deleteUserById(int userId);
}
