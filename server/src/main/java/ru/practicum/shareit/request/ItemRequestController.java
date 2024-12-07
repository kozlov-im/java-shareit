package ru.practicum.shareit.request;

import lombok.AllArgsConstructor;
import org.springframework.web.bind.annotation.*;
import ru.practicum.shareit.request.dto.ItemRequestCreateDto;
import ru.practicum.shareit.request.dto.ItemRequestDtoWithAnswer;
import ru.practicum.shareit.request.model.ItemRequest;
import ru.practicum.shareit.request.service.ItemRequestService;

import java.util.Collection;

@RestController
@RequestMapping(path = "/requests")
@AllArgsConstructor
public class ItemRequestController {
    private final ItemRequestService itemRequestService;

    @PostMapping
    public ItemRequest addRequest(@RequestHeader("X-Sharer-User-Id") int userId,
                                  @RequestBody ItemRequestCreateDto itemRequestCreateDto) {
        return itemRequestService.addRequest(userId, itemRequestCreateDto);
    }

    @GetMapping
    public Collection<ItemRequestDtoWithAnswer> getRequests(@RequestHeader("X-Sharer-User-Id") int userId) {
        return itemRequestService.getRequests(userId);
    }

    @GetMapping("/all")
    public Collection<ItemRequest> getRequestsForAllUsers() {
        return itemRequestService.getRequestsForAllUsers();
    }

    @GetMapping("/{requestId}")
    public ItemRequestDtoWithAnswer getRequestData(@PathVariable int requestId) {
        return itemRequestService.getRequestData(requestId);
    }

}
