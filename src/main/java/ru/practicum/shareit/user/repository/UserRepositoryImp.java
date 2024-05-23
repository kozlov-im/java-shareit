package ru.practicum.shareit.user.repository;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;
import ru.practicum.shareit.exception.BadRequestException;
import ru.practicum.shareit.exception.InternalServerErrorException;
import ru.practicum.shareit.user.User;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

@Repository
@RequiredArgsConstructor
public class UserRepositoryImp implements UserRepository {
    private List<User> users = new ArrayList<>();
    private int id = 1;

    @Override
    public User saveUser(User user) {
        if (user.getEmail() == null || user.getEmail().isEmpty()) {
            throw new BadRequestException("email is empty");
        }
        for (User userInArray : users) {
            if (userInArray.getEmail().equals(user.getEmail())) {
                throw new InternalServerErrorException("email " + user.getEmail() + " already exist");
            }
        }
        user.setId(id);
        id = id + 1;
        users.add(user);
        return user;
    }

    @Override
    public Collection<User> getAllUsers() {
        return users;
    }

    @Override
    public User getUserById(int userId) {
        for (User userInArray : users) {
            if (userInArray.getId() == userId) {
                return userInArray;
            }
        }
        throw new InternalServerErrorException("userId = " + userId + " isn't found");
    }

    @Override
    public User updateUser(int userId, User user) {
        for (User userInArray : users) {
            if (userInArray.getId() == userId) {
                if (String.valueOf(user.getName()) != "null") {
                    userInArray.setName(user.getName());
                }
                if (String.valueOf(user.getEmail()) != "null") {
                    for (User userEmailInArray : users) {
                        if ((userEmailInArray.getEmail().equals(user.getEmail())) && (userEmailInArray.getId() != userId)) {
                            throw new InternalServerErrorException("email " + user.getEmail() + " already exist");
                        }
                    }
                    userInArray.setEmail(user.getEmail());
                }
                return userInArray;
            }
        }
        throw new InternalServerErrorException("user id = " + userId + " isn't found");
    }

    @Override
    public void deleteUserById(int userId) {
        User userForDelete = null;
        for (User userInArray : users) {
            if (userInArray.getId() == userId) {
                userForDelete = userInArray;
                break;
            }
        }
        if (!userForDelete.equals(null)) {
            users.remove(userForDelete);
        } else {
            throw new InternalServerErrorException("userId = " + userId + " isn't found");
        }

    }
}
