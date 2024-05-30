package ru.practicum.shareit.user.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ru.practicum.shareit.user.User;
import ru.practicum.shareit.user.repository.UserRepository;

import java.util.Collection;

@Service
@RequiredArgsConstructor
public class UserServiceIml implements UserService {
    private final UserRepository repository;

    @Override
    public User saveUser(User user) {
        return repository.saveUser(user);
    }

    @Override
    public Collection<User> getAllUsers() {
        return repository.getAllUsers();
    }

    @Override
    public User getUserById(int userId) {
        return repository.getUserById(userId);
    }

    @Override
    public User updateUser(int userId, User user) {
        return repository.updateUser(userId, user);
    }

    @Override
    public void deleteUserById(int userId) {
        repository.deleteUserById(userId);

    }
}
