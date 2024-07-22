package ru.practicum.shareit.item.service;

import ru.practicum.shareit.item.dto.ItemCreateDto;
import ru.practicum.shareit.item.model.Item;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Map;

public interface ItemService {

    Item createItem(int userId, ItemCreateDto itemCreateDto);

    Item updateItem(int userId, int itemId, ItemCreateDto itemCreateDto);

    Map<Integer, ArrayList<Item>> getAllItems();

    Item getItemById(int itemId);

    Collection<Item> getItemsForUser(int userId);

    Collection<Item> searchItems(String text);
}
