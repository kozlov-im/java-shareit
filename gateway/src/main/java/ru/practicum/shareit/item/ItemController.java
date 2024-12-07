package ru.practicum.shareit.item;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import ru.practicum.shareit.item.dto.*;


@Controller
@RequestMapping(path = "/items")
@RequiredArgsConstructor
@Slf4j
@Validated
public class ItemController {
    private final ItemClient itemClient;

    @PostMapping
    public ResponseEntity<Object> createItem(@RequestHeader("X-Sharer-User-Id") long userId,
                                           @RequestBody @Valid ItemCreateDto itemCreateDto) {
        log.info("Create item {}", itemCreateDto);
        return itemClient.createItem(userId, itemCreateDto);
    }

    @PatchMapping("/{itemId}")
    public ResponseEntity<Object> updateItem(@RequestHeader("X-Sharer-User-Id") long userId,
                                             @PathVariable long itemId,
                                             @RequestBody ItemUpdateDto itemUpdateDto) {
        log.info("Update item itemId={} for user userId={}", itemId, userId);
        return itemClient.updateItem(userId, itemId, itemUpdateDto);
    }

    @GetMapping("/{itemId}")
    public ResponseEntity<Object> getItems(@RequestHeader("X-Sharer-User-Id") long userId,
                                           @PathVariable long itemId) {
        log.info("Get item itemId={}", itemId);
        return itemClient.getItems(userId, itemId);
    }


    @GetMapping
    public ResponseEntity<Object> getItemsForUser(@RequestHeader("X-Sharer-User-Id") long userId) {
        log.info("Get items for user userId={}", userId);
        return itemClient.getItemsForUser(userId);
    }

    @GetMapping("/search")
    public ResponseEntity<Object> searchItems(@RequestParam String text) {
        log.info("Search text {}", text);
        return itemClient.search(text);
    }


    @PostMapping("/{itemId}/comment")
    public ResponseEntity<Object> addComment(@RequestHeader("X-Sharer-User-Id") int userId,
                                             @PathVariable int itemId, @RequestBody CommentCreateDto comment) {
        return itemClient.addComment(userId, itemId, comment);
    }

}
