package ru.practicum.shareit.request.service;

import ru.practicum.shareit.request.dto.ItemRequestCreateDto;
import ru.practicum.shareit.request.dto.ItemRequestDtoWithAnswer;
import ru.practicum.shareit.request.model.ItemRequest;

import java.util.Collection;

public interface ItemRequestService {
    ItemRequest addRequest(int userId, ItemRequestCreateDto itemRequestCreateDto);

    Collection<ItemRequestDtoWithAnswer> getRequests(int userId);

    Collection<ItemRequest> getRequestsForAllUsers();

    ItemRequestDtoWithAnswer getRequestData(int requestId);

}
