package ru.practicum.shareit.item;

import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;
import ru.practicum.shareit.item.dto.ItemDto;
import ru.practicum.shareit.item.mapper.ItemMapper;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.item.service.ItemService;

import javax.validation.Valid;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Map;

@RestController
@RequestMapping("/items")
@RequiredArgsConstructor
public class ItemController {
    private final ItemService itemService;

    @PostMapping
    public Item createItem(@Valid @RequestHeader("X-Sharer-User-Id") int userId, @Valid @RequestBody ItemDto itemDto) {
        return itemService.createItem(userId, ItemMapper.toItem(itemDto));
    }

    @PatchMapping("/{itemId}")
    public Item updateItem(@Valid @RequestHeader("X-Sharer-User-Id") int userId, @RequestBody ItemDto itemDto,
                           @PathVariable int itemId) {
        return itemService.updateItem(userId, itemId, ItemMapper.toItem(itemDto));
    }

    @GetMapping("/all")
    public Map<Integer, ArrayList<Item>> getAllItems() {
        return itemService.getAllItems();
    }

    @GetMapping("/{itemId}")
    public ItemDto getItemById(@PathVariable int itemId) {
        return ItemMapper.toItemDto(itemService.getItemById(itemId));
    }

    @GetMapping
    public Collection<Item> getItemsForUser(@Valid @RequestHeader("X-Sharer-User-Id") int userId) {
        return itemService.getItemsForUser(userId);
    }

    @GetMapping("/search")
    public Collection<Item> searchItems(@RequestParam String text) {
        return itemService.searchItems(text);
    }

}
