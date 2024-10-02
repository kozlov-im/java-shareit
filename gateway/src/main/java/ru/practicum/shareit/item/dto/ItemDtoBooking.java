package ru.practicum.shareit.item.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import ru.practicum.shareit.booking.dto.BookingDto;

import java.util.Collection;

@Data
@AllArgsConstructor
public class ItemDtoBooking {
    private int id;
    private String name;
    private String description;
    private Boolean available;
    private Integer request;
    private BookingDto lastBooking;
    private BookingDto nextBooking;
    private Collection<CommentDto> comments;
}
