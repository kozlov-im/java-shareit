package ru.practicum.shareit.request;

import lombok.Data;
import ru.practicum.shareit.user.User;

import java.util.Date;

@Data
public class ItemRequest {
    private int id;
    private String description;
    private User requestor;
    private Date created;
}
