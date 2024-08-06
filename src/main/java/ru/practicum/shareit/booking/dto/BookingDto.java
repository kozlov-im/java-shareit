package ru.practicum.shareit.booking.dto;

import jakarta.validation.constraints.NotNull;
import lombok.*;
import ru.practicum.shareit.booking.Status;

import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class BookingDto {
    private int id;
    @NotNull(message = "start time is empty")
    private LocalDateTime start;
    @NotNull(message = "end time is empty")
    private LocalDateTime end;
    private int itemId;
    private int bookerId;
    private Status status;
}
