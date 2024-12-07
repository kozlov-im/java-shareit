package ru.practicum.shareit.request.dto;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class ItemRequestShortDto {
    private int id;
    private String name;
    private int ownerId;

}
