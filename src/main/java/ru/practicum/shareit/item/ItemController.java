package ru.practicum.shareit.item;

import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import org.springframework.web.bind.annotation.*;
import ru.practicum.shareit.item.dto.*;
import ru.practicum.shareit.item.mapper.ItemMapper;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.item.service.ItemService;

import java.util.ArrayList;
import java.util.Collection;

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

    @GetMapping("/{itemId}")
    public ItemDtoBooking getItemByIdAndUserId(@RequestHeader("X-Sharer-User-Id") int userId, @PathVariable int itemId) {
        return itemService.getItemByIdAndUserId(userId, itemId);
    }

    @GetMapping
    public Collection<ItemDtoBooking> getItemsForUser(@RequestHeader("X-Sharer-User-Id") int userId) {
        return itemService.getItemsForUser(userId);
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

    @PostMapping("/{itemId}/comment")
    public CommentDto addComment(@RequestHeader("X-Sharer-User-Id") int userId, @PathVariable int itemId, @RequestBody CommentCreateDto comment) {
        return itemService.addComment(userId, itemId, comment);
    }

}
