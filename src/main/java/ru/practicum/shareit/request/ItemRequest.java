package ru.practicum.shareit.request;

import ru.practicum.shareit.user.User;

import lombok.Data;

import java.util.Date;

/**
 * TODO Sprint add-item-requests.
 */
@Data
public class ItemRequest {
    private int id;
    private String description;
    private User requestor;
    private Date created;

}
