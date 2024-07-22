package ru.practicum.shareit.user;

import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import ru.practicum.shareit.exception.Marker;
import ru.practicum.shareit.user.dto.UserCreateDto;
import ru.practicum.shareit.user.dto.UserDto;
import ru.practicum.shareit.user.mapper.UserMapper;
import ru.practicum.shareit.user.service.UserService;

import java.util.ArrayList;
import java.util.Collection;

@RestController
@RequestMapping(path = "/users")
@AllArgsConstructor
public class UserController {
    private final UserService userService;

    @PostMapping
    public UserDto createUser(@Validated({Marker.OnCreate.class}) @RequestBody UserCreateDto userCreateDto) {
        return UserMapper.toUserDto(userService.saveUser(userCreateDto));
    }

    @PatchMapping("/{userId}")
    public UserDto updateUser(@Valid @RequestBody UserCreateDto userCreateDto, @PathVariable int userId) {
        return UserMapper.toUserDto(userService.updateUser(userId, userCreateDto));
    }

    @GetMapping
    public Collection<UserDto> getAllUsers() {
        Collection<UserDto> usersDto = new ArrayList<>();
        for (User user : userService.getAllUsers()) {
            usersDto.add(UserMapper.toUserDto(user));
        }
        return usersDto;
    }

    @GetMapping("/{userId}")
    public UserDto getUserById(@PathVariable int userId) {
        return UserMapper.toUserDto(userService.getUserById(userId));
    }

    @DeleteMapping("/{userId}")
    public void deleteUserById(@PathVariable int userId) {
        userService.deleteUserById(userId);
    }

}
