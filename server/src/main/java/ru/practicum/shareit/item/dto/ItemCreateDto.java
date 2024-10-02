package ru.practicum.shareit.item.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import ru.practicum.shareit.user.User;

@Data
@AllArgsConstructor
public class ItemCreateDto {
    private String name;
    private String description;
    private Boolean available;
    private User owner;
    private Integer request;
}
