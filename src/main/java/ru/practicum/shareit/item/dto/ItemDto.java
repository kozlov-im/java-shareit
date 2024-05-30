package ru.practicum.shareit.item.dto;

import lombok.AllArgsConstructor;
import lombok.Data;

import javax.validation.constraints.NotBlank;

/**
 * TODO Sprint add-controllers.
 */
@Data
@AllArgsConstructor
public class ItemDto {
    private int id;
    @NotBlank(message = "name is empty")
    private String name;
    @NotBlank(message = "description is empty")
    private String description;
    private Boolean available;
    private String request;
}
