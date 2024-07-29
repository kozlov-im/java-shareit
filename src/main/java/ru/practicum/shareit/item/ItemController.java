package ru.practicum.shareit.item;

import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import org.springframework.web.bind.annotation.*;
import ru.practicum.shareit.item.dto.ItemCreateDto;
import ru.practicum.shareit.item.dto.ItemDto;
import ru.practicum.shareit.item.mapper.ItemMapper;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.item.service.ItemService;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/items")
@AllArgsConstructor
public class ItemController {
    private final ItemService itemService;

    @PostMapping
    public ItemDto createItem(@RequestHeader("X-Sharer-User-Id") int userId, @Valid @RequestBody ItemCreateDto itemCreateDto) {
        return ItemMapper.toItemDto(itemService.createItem(userId, itemCreateDto));
    }

    @PatchMapping("/{itemId}")
    public ItemDto updateItem(@RequestHeader("X-Sharer-User-Id") int userId, @PathVariable int itemId,
                              @RequestBody ItemCreateDto itemCreateDto) {
        return ItemMapper.toItemDto(itemService.updateItem(userId, itemId, itemCreateDto));
    }

    @GetMapping("/all")
    public Map<Integer, ArrayList<ItemDto>> getAllItems() {
        Map<Integer, ArrayList<ItemDto>> itemsDto = new HashMap<>();
        ArrayList<ItemDto> itemDtoList = new ArrayList<>();
        for (Integer key : itemService.getAllItems().keySet()) {
            for (Item item : itemService.getAllItems().get(key)) {
                ItemDto itemDto = ItemMapper.toItemDto(item);
                itemDtoList.add(itemDto);
            }
            itemsDto.put(key, itemDtoList);
        }
        return itemsDto;
    }

    @GetMapping("/{itemId}")
    public ItemDto getItemById(@PathVariable int itemId) {
        return ItemMapper.toItemDto(itemService.getItemById(itemId));
    }

    @GetMapping
    public Collection<ItemDto> getItemsForUser(@RequestHeader("X-Sharer-User-Id") int userId) {
        Collection<ItemDto> itemsDto = new ArrayList<>();
        for (Item item : itemService.getItemsForUser(userId)) {
            ItemDto itemDto = ItemMapper.toItemDto(item);
            itemsDto.add(itemDto);
        }
        return itemsDto;
    }

    @GetMapping("/search")
    public Collection<ItemDto> searchItems(@RequestParam String text) {
        Collection<ItemDto> itemsDto = new ArrayList<>();
        for (Item item : itemService.searchItems(text)) {
            ItemDto itemDto = ItemMapper.toItemDto(item);
            itemsDto.add(itemDto);
        }
        return itemsDto;
    }

}
