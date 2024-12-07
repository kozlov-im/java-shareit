package ru.practicum.shareit.item.mapper;

import ru.practicum.shareit.booking.dto.BookingCreateDto;
import ru.practicum.shareit.booking.dto.BookingDto;
import ru.practicum.shareit.item.dto.*;
import ru.practicum.shareit.item.model.Comment;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.request.dto.ItemRequestShortDto;

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
                itemCreateDto.getAvailable(),
                itemCreateDto.getRequestId()
        );
    }

    public static Item toItemModel(ItemUpdateDto itemUpdateDto) {
        return new Item(
                itemUpdateDto.getName(),
                itemUpdateDto.getDescription(),
                itemUpdateDto.getAvailable(),
                itemUpdateDto.getRequestId()
        );
    }

    public static ItemBookingDto toItemDtoBooking(Item item, BookingDto lastBooking, BookingDto nextBooking, Collection<CommentDto> commentsDto) {
        return new ItemBookingDto(
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

    public static ItemCommentDto toItemDtoComment(Item item, BookingCreateDto lastBooking, BookingCreateDto nextBooking, Comment comment) {
        return new ItemCommentDto(
                item.getId(),
                item.getName(),
                item.getDescription(),
                item.getAvailable(),
                lastBooking,
                nextBooking,
                comment
        );
    }

    public static ItemRequestShortDto toItemDtoRequest(Item item) {
        return new ItemRequestShortDto(
                item.getId(),
                item.getName(),
                item.getOwner().getId()
        );
    }
}
