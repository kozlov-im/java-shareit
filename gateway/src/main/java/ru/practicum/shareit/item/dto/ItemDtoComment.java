package ru.practicum.shareit.item.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import ru.practicum.shareit.booking.dto.BookingCreateDto;
import ru.practicum.shareit.item.model.Comment;

@Data
@AllArgsConstructor
public class ItemDtoComment {
    private int id;
    private String name;
    private String description;
    private Boolean available;
    private BookingCreateDto lastBooking;
    private BookingCreateDto nextBooking;
    private Comment comment;
}
