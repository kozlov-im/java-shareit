package ru.practicum.shareit.item.service;

import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;
import ru.practicum.shareit.exception.NotFoundException;
import ru.practicum.shareit.item.dto.ItemDto;
import ru.practicum.shareit.item.mapper.ItemMapper;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.item.repository.ItemRepository;
import ru.practicum.shareit.user.User;
import ru.practicum.shareit.user.repository.UserRepository;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Map;

@Service
@AllArgsConstructor
public class ItemServiceImpl implements ItemService {
    private final ItemRepository itemRepository;
    private final UserRepository userRepository;

    @Override
    public Item createItem(int userId, ItemDto itemDto) {
        Item item = ItemMapper.toItemModel(itemDto);
        checkUserExist(userId);
        item.setOwner(userRepository.getUserById(userId));
        return itemRepository.createItem(userId, item);
    }

    @Override
    public Item updateItem(int userId, int itemId, ItemDto itemDto) {
        Item item = ItemMapper.toItemModel(itemDto);
        checkUserExist(userId);
        Item itemExistForUpdate = checkItemForUserExist(userId, itemId);
        if (String.valueOf(item.getName()) != "null") {
            itemExistForUpdate.setName(item.getName());
        }
        if (String.valueOf(item.getDescription()) != "null") {
            itemExistForUpdate.setDescription(item.getDescription());
        }
        if (String.valueOf(item.getAvailable()) != "null") {
            itemExistForUpdate.setAvailable(item.getAvailable());
        }
        if (String.valueOf(item.getRequest()) != "null") {
            itemExistForUpdate.setRequest(item.getRequest());
        }
        return itemRepository.updateItem(userId, itemId, itemExistForUpdate);
    }

    @Override
    public Map<Integer, ArrayList<Item>> getAllItems() {
        return itemRepository.getAllItems();
    }

    @Override
    public Item getItemById(int itemId) {
        return itemRepository.getItemById(itemId);
    }

    @Override
    public Collection<Item> getItemsForUser(int userId) {
        checkUserExist(userId);
        return itemRepository.getItemsForUser(userId);
    }

    @Override
    public Collection<Item> searchItems(String text) {
        if (text.isEmpty()) {
            return new ArrayList<>();
        } else {
            return itemRepository.searchItems(text);
        }
    }

    public void checkUserExist(int userId) {
        for (User userInArray : userRepository.getAllUsers()) {
            if (userInArray.getId() == userId) {
                return;
            }
        }
        throw new NotFoundException("userId = " + userId + " isn't found");
    }

    public Item checkItemForUserExist(int userId, int itemId) {
        Collection<Item> itemForUser = itemRepository.getAllItems().get(userId);
        if (CollectionUtils.isEmpty(itemForUser)) {
            throw new NotFoundException("userId = " + userId + " doesn't have any item");
        }
        for (Item itemInArray : itemForUser) {
            if (itemInArray.getId() == itemId) {
                return itemInArray;
            }
        }
        throw new NotFoundException("userId = " + userId + " doesn't have itemId = " + itemId);

    }
}
