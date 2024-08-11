package ru.practicum.shareit.item.mapper;

import ru.practicum.shareit.booking.dto.BookingCreateDto;
import ru.practicum.shareit.booking.dto.BookingDto;
import ru.practicum.shareit.item.dto.*;
import ru.practicum.shareit.item.model.Comment;
import ru.practicum.shareit.item.model.Item;

import java.util.Collection;

public class ItemMapper {

    public static ItemDto toItemDto(Item item) {
        return new ItemDto(
                item.getId(),
                item.getName(),
                item.getDescription(),
                item.getAvailable(),
                item.getOwner(),
                item.getRequest()
        );
    }

    public static Item toItemModel(ItemCreateDto itemCreateDto) {
        return new Item(
                itemCreateDto.getName(),
                itemCreateDto.getDescription(),
                itemCreateDto.getAvailable()
        );
    }

    public static ItemDtoBooking toItemDtoBooking(Item item, BookingDto lastBooking, BookingDto nextBooking, Collection<CommentDto> commentsDto) {
        return new ItemDtoBooking(
                item.getId(),
                item.getName(),
                item.getDescription(),
                item.getAvailable(),
                item.getRequest(),
                lastBooking,
                nextBooking,
                commentsDto
        );
    }

    public static ItemDtoComment toItemDtoComment(Item item, BookingCreateDto lastBooking, BookingCreateDto nextBooking, Comment comment) {
        return new ItemDtoComment(
                item.getId(),
                item.getName(),
                item.getDescription(),
                item.getAvailable(),
                lastBooking,
                nextBooking,
                comment
        );
    }
}
