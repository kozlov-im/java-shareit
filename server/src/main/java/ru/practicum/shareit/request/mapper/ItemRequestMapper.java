package ru.practicum.shareit.request.mapper;

import ru.practicum.shareit.request.dto.ItemRequestShortDto;
import ru.practicum.shareit.request.dto.ItemRequestCreateDto;
import ru.practicum.shareit.request.dto.ItemRequestDtoWithAnswer;
import ru.practicum.shareit.request.model.ItemRequest;
import ru.practicum.shareit.user.User;

import java.time.LocalDateTime;
import java.util.Collection;

public class ItemRequestMapper {

    public static ItemRequest toItemRequestModel(ItemRequestCreateDto itemRequestCreateDto,
                                                 User user, LocalDateTime created) {
        return new ItemRequest(itemRequestCreateDto.getDescription(), user, created);
    }

    public static ItemRequestDtoWithAnswer toItemRequestDtoWithAnswer(ItemRequest itemRequest,
                                                                      Collection<ItemRequestShortDto> itemRequestShortDtos) {
        return new ItemRequestDtoWithAnswer(
                itemRequest.getId(),
                itemRequest.getDescription(),
                itemRequest.getCreated(),
                itemRequestShortDtos
                );
    }
}
