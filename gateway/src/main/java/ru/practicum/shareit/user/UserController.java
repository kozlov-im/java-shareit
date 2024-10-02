package ru.practicum.shareit.user;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import ru.practicum.shareit.exception.Marker;
import ru.practicum.shareit.user.dto.UserCreateDto;


@Controller
@RequestMapping(path = "/users")
@RequiredArgsConstructor
@Slf4j
@Validated
public class UserController {

    private final UserClient userClient;

    @PostMapping
    public ResponseEntity<Object> createUser(@Validated({Marker.OnCreate.class}) @RequestBody UserCreateDto userCreateDto) {
        log.info("Create user (it is gateway)");
        return userClient.createUser(userCreateDto);
    }

    @PatchMapping("/{userId}")
    public ResponseEntity<Object> updateUser(@Valid @RequestBody UserCreateDto userCreateDto,
                                             @PathVariable long userId) {
        log.info("Update user userId={}", userId);
        return userClient.updateUser(userCreateDto, userId);
    }

    @GetMapping
    public ResponseEntity<Object> getUsers() {
        log.info("Get users (it is gateway)");
        return userClient.getUsers();
    }

    @GetMapping("/{userId}")
    public ResponseEntity<Object> getUserById(@PathVariable Long userId) {
        log.info("Get user userId={}", userId);
        return userClient.getUserById(userId);
    }

    @DeleteMapping("/{userId}")
    public ResponseEntity<Object> deleteUserById(@PathVariable Long userId) {
        log.info("Delete user userId={}", userId);
        return userClient.deleteUserById(userId);
    }

}
