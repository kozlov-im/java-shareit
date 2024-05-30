package ru.practicum.shareit.user;

import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;
import ru.practicum.shareit.user.dto.UserDto;
import ru.practicum.shareit.user.mapper.UserMapper;
import ru.practicum.shareit.user.service.UserService;

import javax.validation.Valid;
import java.util.ArrayList;
import java.util.Collection;

@RestController
@RequestMapping(path = "/users")
@RequiredArgsConstructor
public class UserController {
    private final UserService userService;

    @PostMapping
    public User createUser(@Valid @RequestBody UserDto userDto) {
        return userService.saveUser(UserMapper.toUser(userDto));
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

    @PatchMapping("/{userId}")
    public User updateUser(@Valid @RequestBody UserDto userDto, @PathVariable int userId) {
        return userService.updateUser(userId, UserMapper.toUser(userDto));
    }

    @DeleteMapping("/{userId}")
    public void deleteUserById(@PathVariable int userId) {
        userService.deleteUserById(userId);
    }

}
