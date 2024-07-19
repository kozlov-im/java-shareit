package ru.practicum.shareit.user.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Data;
import ru.practicum.shareit.exception.Marker;

@Data
@AllArgsConstructor
public class UserDto {
    private String name;
    @NotBlank(message = "email should not be blank", groups = {Marker.OnCreate.class})
    @Email(message = "email incorrect", groups = {Marker.OnCreate.class, Marker.onUpdate.class})
    private String email;
}