package ru.practicum.shareit.item.repository;

import org.springframework.stereotype.Repository;
import ru.practicum.shareit.exception.NotFoundException;
import ru.practicum.shareit.item.model.Item;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

@Repository
public class ItemRepositoryImpl implements ItemRepository {
    private Map<Integer, ArrayList<Item>> items = new HashMap<>();
    private int itemId = 1;

    @Override
    public Item createItem(int userId, Item item) {
        item.setId(itemId);
        itemId++;
        if (items.containsKey(userId)) {
            items.get(userId).add(item);
        } else {
            items.put(userId, new ArrayList<>());
            items.get(userId).add(item);
        }
        return item;
    }

    @Override
    public Item updateItem(int userId, int itemId, Item item) {
        for (Item itemInArray : items.get(userId)) {
            if (itemInArray.getId() == itemId) {
                itemInArray.setName(item.getName());
                itemInArray.setDescription(item.getDescription());
                itemInArray.setAvailable(item.getAvailable());
                itemInArray.setRequest(item.getRequest());
            }
        }
        return item;
    }

    @Override
    public Map<Integer, ArrayList<Item>> getAllItems() {
        return items;
    }

    @Override
    public Item getItemById(int itemId) {
        for (ArrayList<Item> itemsArray : items.values()) {
            for (Item itemInArray : itemsArray) {
                if (itemInArray.getId() == itemId) {
                    return itemInArray;
                }
            }
        }
        throw new NotFoundException("itemId = " + itemId + " isn't found");

    }

    @Override
    public Collection<Item> getItemsForUser(int userId) {
        return items.get(userId);
    }

    @Override
    public Collection<Item> searchItems(String text) {
        Collection<Item> result = new ArrayList<>();
        String[] words = text.split(" ");
        for (ArrayList<Item> itemsArray : items.values()) {
            for (Item itemInArray : itemsArray) {
                for (String word : words) {
                    if ((itemInArray.getName().toLowerCase().contains(word.toLowerCase()) ||
                            itemInArray.getDescription().toLowerCase().contains(word.toLowerCase())) &&
                            itemInArray.getAvailable()) {
                        result.add(itemInArray);
                        break;
                    }
                }
            }
        }
        return result;
    }
}
