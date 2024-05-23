package ru.practicum.shareit.item.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class Item {
    private int id;
    private String name;
    private String description;
    private Boolean available;
    private int owner;
    private String request;

    public Item(String name, String description, Boolean available, String request) {
        this.name = name;
        this.description = description;
        this.available = available;
        this.request = request;
    }
}
