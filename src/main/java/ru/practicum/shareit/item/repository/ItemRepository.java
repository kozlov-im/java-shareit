package ru.practicum.shareit.item.repository;

import ru.practicum.shareit.item.model.Item;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Map;

public interface ItemRepository {
    Item createItem(int userId, Item item);

    Item updateItem(int userId, int itemId, Item item);

    Map<Integer, ArrayList<Item>> getAllItems();

    Item getItemById(int itemId);

    Collection<Item> getItemsForUser(int userId);

    Collection<Item> searchItems(String text);
}
