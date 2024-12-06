package ru.practicum.shareit.request.service;

import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import ru.practicum.shareit.item.dto.ItemDtoRequest;
import ru.practicum.shareit.item.mapper.ItemMapper;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.item.repository.ItemRepository;
import ru.practicum.shareit.request.dto.ItemRequestCreateDto;
import ru.practicum.shareit.request.dto.ItemRequestDtoWithAnswer;
import ru.practicum.shareit.request.mapper.ItemRequestMapper;
import ru.practicum.shareit.request.model.ItemRequest;
import ru.practicum.shareit.request.repository.ItemRequestRepository;
import ru.practicum.shareit.user.User;
import ru.practicum.shareit.user.service.UserService;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Collection;

@Service
@AllArgsConstructor
public class ItemRequestServiceImpl implements ItemRequestService {
    private final UserService userService;
    private final ItemRequestRepository itemRequestRepository;
    private final ItemRepository itemRepository;

    @Override
    public ItemRequest addRequest(int userId, ItemRequestCreateDto itemRequestCreateDto) {
        userService.checkUserExist(userId);
        User user = userService.getUserById(userId);
        LocalDateTime created = LocalDateTime.now().withNano(0);
        ItemRequest itemRequest = ItemRequestMapper.toItemRequestModel(itemRequestCreateDto, user, created);
        return itemRequestRepository.save(itemRequest);
    }

    @Override
    public Collection<ItemRequestDtoWithAnswer> getRequests(int userId) {
        userService.checkUserExist(userId);
        Collection<ItemRequest> itemRequestsArray = itemRequestRepository.getRequestsForUser(userId);
        Collection<ItemRequestDtoWithAnswer> itemRequestDtoWithAnswers = new ArrayList<>();

        for (ItemRequest itemRequest : itemRequestsArray) {
            Collection<Item> items = itemRepository.findByRequest(itemRequest.getId());
            Collection<ItemDtoRequest> itemDtoRequests = new ArrayList<>();
            for (Item itemElement : items) {
                itemDtoRequests.add(ItemMapper.toItemDtoRequest(itemElement));
            }
            itemRequestDtoWithAnswers.add(ItemRequestMapper.toItemRequestDtoWithAnswer(itemRequest, itemDtoRequests));

        }
        return itemRequestDtoWithAnswers;
    }

    @Override
    public Collection<ItemRequest> getRequestsForAllUsers() {
        return itemRequestRepository.findAll();
    }

    @Override
    public ItemRequestDtoWithAnswer getRequestData(int requestId) {
        ItemRequest itemRequest = itemRequestRepository.getReferenceById(requestId);
        Collection<Item> items = itemRepository.findByRequest(itemRequest.getId());
        Collection<ItemDtoRequest> itemDtoRequests = new ArrayList<>();
        for (Item itemElement : items) {
            itemDtoRequests.add(ItemMapper.toItemDtoRequest(itemElement));
        }
        return ItemRequestMapper.toItemRequestDtoWithAnswer(itemRequest, itemDtoRequests);
    }

}