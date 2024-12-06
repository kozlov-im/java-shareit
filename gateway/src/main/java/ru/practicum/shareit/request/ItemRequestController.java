package ru.practicum.shareit.request;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import ru.practicum.shareit.request.dto.ItemRequestCreateDto;
//import ru.practicum.shareit.request.service.ItemRequestService;


@Controller
@RequestMapping(path = "/requests")
@RequiredArgsConstructor
@Slf4j
@Validated
public class ItemRequestController {
    //private final ItemRequestService itemRequestService;
    private final ItemRequestClient itemRequestClient;

    @PostMapping
    public ResponseEntity<Object> addRequest(@RequestHeader("X-Sharer-User-Id") int userId,
                                             @RequestBody ItemRequestCreateDto itemRequestCreateDto) {
        log.info("addRequest for userId={}", userId);
        return itemRequestClient.addRequest(userId, itemRequestCreateDto);
    }

    @GetMapping
    public  ResponseEntity<Object> getRequests(@RequestHeader("X-Sharer-User-Id") int userId) {
        return itemRequestClient.getRequests(userId);
    }

    @GetMapping("/all")
    public ResponseEntity<Object> getRequestsForAllUsers() {
        log.info("getRequestsForAllUsers");
        return itemRequestClient.getRequestsForAllUsers();
    }


    @GetMapping("/{requestId}")
    public ResponseEntity<Object> getRequestData(@PathVariable int requestId) {
        return itemRequestClient.getRequestData(requestId);
    }
}
