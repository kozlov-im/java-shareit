package ru.practicum.shareit.item.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ru.practicum.shareit.exception.BadRequestException;
import ru.practicum.shareit.exception.NotFoundException;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.item.repository.ItemRepository;
import ru.practicum.shareit.user.User;
import ru.practicum.shareit.user.repository.UserRepository;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Map;

@Service
@RequiredArgsConstructor
public class ItemServiceImpl implements ItemService {

    private final ItemRepository itemRepository;
    private final UserRepository userRepository;

    @Override
    public Item createItem(int userId, Item item) {
        if (String.valueOf(item.getAvailable()).equals("null")) {
            throw new BadRequestException("available is null");
        }
        checkUserExist(userId);
        return itemRepository.createItem(userId, item);
    }

    @Override
    public Item updateItem(int userId, int itemId, Item item) {
        checkUserExist(userId);
        return itemRepository.updateItem(userId, itemId, item);
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
}
