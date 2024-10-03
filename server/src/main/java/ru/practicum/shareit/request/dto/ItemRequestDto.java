package ru.practicum.shareit.request.dto;

import jakarta.persistence.*;
import lombok.Data;
import ru.practicum.shareit.user.User;

import java.util.Date;

@Data
public class ItemRequestDto {
    private int id;
    private String description;
    private User user;
    private Date created;
}
