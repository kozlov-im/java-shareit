package ru.practicum.shareit.item.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class ItemUpdateDto {
    @NotBlank(message = "name is empty")
    private String name;
    @NotBlank(message = "description is empty")
    private String description;
    @NotNull(message = "available should not be null")
    private Boolean available;
    private int requestId;
}
