package ru.practicum.shareit.item.repository;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;
import ru.practicum.shareit.exception.NotFoundException;
import ru.practicum.shareit.item.model.Item;

import java.util.*;

@Repository
@RequiredArgsConstructor
public class ItemRepositoryImpl implements ItemRepository {
    private Map<Integer, ArrayList<Item>> items = new HashMap<>();
    private int itemId = 1;

    @Override
    public Item createItem(int userId, Item item) {
        item.setId(itemId);
        item.setOwner(userId);
        itemId = itemId + 1;
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
        if (items.containsKey(userId)) {
            for (Item itemInArray : items.get(userId)) {
                if (itemInArray.getId() == itemId) {
                    if (String.valueOf(item.getName()) != "null") {
                        itemInArray.setName(item.getName());
                    }
                    if (String.valueOf(item.getDescription()) != "null") {
                        itemInArray.setDescription(item.getDescription());
                    }
                    if (String.valueOf(item.getAvailable()) != "null") {
                        itemInArray.setAvailable(item.getAvailable());
                    }
                    if (String.valueOf(item.getRequest()) != "null") {
                        itemInArray.setRequest(item.getRequest());
                    }

                    return itemInArray;
                }
            }
            throw new NotFoundException("itemId = " + item.getId() + " isn't found");
        }

        throw new NotFoundException("userId = " + userId + " haven't had any items yet");
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
