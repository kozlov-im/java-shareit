package ru.practicum.shareit.user.repository;

import org.springframework.stereotype.Repository;
import ru.practicum.shareit.exception.InternalServerErrorException;
import ru.practicum.shareit.user.User;

import java.util.ArrayList;
import java.util.Collection;

@Repository
public class UserRepositoryImpl implements UserRepository {
    private Collection<User> users = new ArrayList<>();
    private int id = 1;

    @Override
    public User saveUser(User user) {
        user.setId(id);
        id++;
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
    public void updateUserName(int userId, User user) {
        getUserById(userId).setName(user.getName());
    }

    @Override
    public void updateUserEmail(int userId, User user) {
        getUserById(userId).setEmail(user.getEmail());
    }

    @Override
    public void deleteUserById(int userId) {
        users.remove(getUserById(userId));
    }

}
