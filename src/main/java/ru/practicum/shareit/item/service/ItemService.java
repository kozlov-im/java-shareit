package ru.practicum.shareit.item.service;

import ru.practicum.shareit.item.dto.*;
import ru.practicum.shareit.item.model.Comment;
import ru.practicum.shareit.item.model.Item;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Map;

public interface ItemService {

    Item createItem(int userId, ItemCreateDto itemCreateDto);

    Item updateItem(int userId, int itemId, ItemCreateDto itemCreateDto);

    Item getItemById(int itemId);

    ItemDtoBooking getItemByIdAndUserId(int userId, int itemId);

    Collection<ItemDtoBooking> getItemsForUser(int userId);

    Collection<Item> searchItems(String text);

    void checkItemExist(int itemId);

    void checkItemAvailable(int itemId);

    CommentDto addComment(int userId, int itemId, CommentCreateDto comment);
}
