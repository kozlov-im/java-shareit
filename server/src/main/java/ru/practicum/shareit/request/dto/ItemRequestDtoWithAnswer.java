package ru.practicum.shareit.request.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import ru.practicum.shareit.item.dto.ItemDtoRequest;


import java.time.LocalDateTime;
import java.util.Collection;

@Data
@AllArgsConstructor
public class ItemRequestDtoWithAnswer {
    private int id;
    private String description;
    private LocalDateTime created;
    private Collection<ItemDtoRequest> items;
}
