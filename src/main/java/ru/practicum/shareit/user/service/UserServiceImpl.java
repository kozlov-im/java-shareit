package ru.practicum.shareit.user.service;

import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import ru.practicum.shareit.exception.ConflictException;
import ru.practicum.shareit.exception.NotFoundException;
import ru.practicum.shareit.user.User;
import ru.practicum.shareit.user.dto.UserCreateDto;
import ru.practicum.shareit.user.mapper.UserMapper;
import ru.practicum.shareit.user.repository.UserRepository;

import java.util.Collection;


@Service
@AllArgsConstructor
public class UserServiceImpl implements UserService {
    private final UserRepository userRepository;

    @Override
    public User saveUser(UserCreateDto userCreateDto) {
        User user = UserMapper.toUserModel(userCreateDto);
        if (userRepository.findByEmail(user.getEmail()) != null) {
            throw new ConflictException("email " + user.getEmail() + " already exist");
        }
        return userRepository.save(user);
    }

    @Override
    public User updateUser(int userId, UserCreateDto userCreateDto) {
        User user = UserMapper.toUserModel(userCreateDto);
        User userForUpdate = getUserById(userId);
        if (user.getName() != null) {
            userForUpdate.setName(user.getName());
        }
        if (user.getEmail() != null) {
            userForUpdate.setEmail(user.getEmail());
        }
        if (userRepository.findByEmail(user.getEmail()) != null) {
            if (userRepository.findByEmail(user.getEmail()).getId() != userId) {
                throw new ConflictException("email " + user.getEmail() + " already exist");
            }
        }
        return userRepository.save(userForUpdate);
    }

    @Override
    public Collection<User> getAllUsers() {
        return userRepository.findAll();
    }

    @Override
    public User getUserById(int userId) {
        return userRepository.getReferenceById(userId);
    }

    @Override
    public void deleteUserById(int userId) {
        userRepository.delete(getUserById(userId));

    }

    @Override
    public void checkUserExist(int userId) {
        if (!userRepository.findById(userId).isPresent()) {
            throw new NotFoundException("userId = " + userId + " isn't found");
        }
    }
}
