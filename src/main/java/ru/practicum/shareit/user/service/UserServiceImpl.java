package ru.practicum.shareit.user.service;

import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import ru.practicum.shareit.exception.InternalServerErrorException;
import ru.practicum.shareit.user.User;
import ru.practicum.shareit.user.dto.UserDto;
import ru.practicum.shareit.user.mapper.UserMapper;
import ru.practicum.shareit.user.repository.UserRepository;

import java.util.Collection;


@Service
@AllArgsConstructor
public class UserServiceImpl implements UserService {
    private final UserRepository userRepository;

    @Override
    public User saveUser(UserDto userDto) {
        User user = UserMapper.toUserModel(userDto);
        for (User userInArray : userRepository.getAllUsers()) {
            if (userInArray.getEmail().equals(user.getEmail())) {
                throw new InternalServerErrorException("email " + user.getEmail() + " already exist");
            }
        }
        return userRepository.saveUser(user);
    }

    @Override
    public User updateUser(int userId, UserDto userDto) {
        User user = UserMapper.toUserModel(userDto);
        if (String.valueOf(user.getName()) != "null") {
            userRepository.updateUserName(userId, user);
        }
        if (String.valueOf(user.getEmail()) != "null") {
            for (User userInArray : userRepository.getAllUsers()) {
                if ((userInArray.getEmail().equals(user.getEmail())) && userInArray.getId() != userId) {
                    throw new InternalServerErrorException("email " + user.getEmail() + " already exist");
                }
            }
            userRepository.updateUserEmail(userId, user);
        }
        return userRepository.getUserById(userId);
    }

    @Override
    public Collection<User> getAllUsers() {
        return userRepository.getAllUsers();
    }

    @Override
    public User getUserById(int userId) {
        return userRepository.getUserById(userId);
    }

    @Override
    public void deleteUserById(int userId) {
        userRepository.deleteUserById(userId);

    }
}
