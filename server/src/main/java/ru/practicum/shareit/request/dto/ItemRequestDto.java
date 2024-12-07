package ru.practicum.shareit.request.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import ru.practicum.shareit.user.User;

import java.time.LocalDate;

@Data
@AllArgsConstructor
public class ItemRequestDto {
    private int id;
    private String description;
    private User user;
    private LocalDate created;
}
