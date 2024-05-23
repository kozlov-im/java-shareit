package ru.practicum.shareit.user.repository;

import ru.practicum.shareit.exception.InternalServerErrorException;
import ru.practicum.shareit.user.User;

import java.util.Collection;

public interface UserRepository {

    User saveUser(User user) throws InternalServerErrorException;

    Collection<User> getAllUsers();

    User getUserById(int userId);

    User updateUser(int userId, User user);

    void deleteUserById(int userId);
}
