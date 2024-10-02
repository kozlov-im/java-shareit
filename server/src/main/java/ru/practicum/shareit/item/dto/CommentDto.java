package ru.practicum.shareit.item.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import ru.practicum.shareit.item.model.Item;

import java.time.LocalDateTime;

@Data
@AllArgsConstructor
public class CommentDto {
    int id;
    String text;
    Item item;
    String authorName;
    LocalDateTime created;
}
