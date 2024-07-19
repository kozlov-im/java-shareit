package ru.practicum.shareit.item.service;

import ru.practicum.shareit.item.dto.ItemDto;
import ru.practicum.shareit.item.model.Item;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Map;

public interface ItemService {

    Item createItem(int userId, ItemDto itemDto);
    Item updateItem(int userId, int itemId, ItemDto itemDto);
    Map<Integer, ArrayList<Item>> getAllItems();
    Item getItemById(int itemId);
    Collection<Item> getItemsForUser(int userId);
    Collection<Item> searchItems(String text);
}
