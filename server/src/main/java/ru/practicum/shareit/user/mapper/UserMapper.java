package ru.practicum.shareit.user.mapper;

import ru.practicum.shareit.user.User;
import ru.practicum.shareit.user.dto.UserCreateDto;
import ru.practicum.shareit.user.dto.UserDto;
import ru.practicum.shareit.user.dto.UserUpdateDto;

public class UserMapper {
    public static UserDto toUserDto(User user) {
        return new UserDto(user.getId(), user.getName(), user.getEmail());
    }

    public static User toUserModel(UserCreateDto userCreateDto) {
        return new User(userCreateDto.getName(), userCreateDto.getEmail());
    }

    public static User toUserModel(UserUpdateDto userUpdateDto) {
        return new User(userUpdateDto.getName(), userUpdateDto.getEmail());
    }
}
