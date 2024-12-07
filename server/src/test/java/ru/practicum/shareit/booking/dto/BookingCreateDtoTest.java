package ru.practicum.shareit.booking.dto;

import lombok.RequiredArgsConstructor;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.json.JsonTest;
import org.springframework.boot.test.json.JacksonTester;
import org.springframework.boot.test.json.JsonContent;

import java.time.LocalDateTime;

import static org.assertj.core.api.Assertions.assertThat;

@JsonTest
@RequiredArgsConstructor(onConstructor_ = @Autowired)
class BookingCreateDtoTest {
    private final JacksonTester<BookingCreateDto> json;

    @Test
    void bookingCreateDtoTest() throws Exception {
        BookingCreateDto bookingCreateDto = new BookingCreateDto(
                LocalDateTime.of(2024, 11, 21, 9, 1, 1),
                LocalDateTime.of(2024, 11, 22, 9, 1, 1),
                1);

        JsonContent<BookingCreateDto> result = json.write(bookingCreateDto);

        assertThat(result).extractingJsonPathStringValue("$.start").isEqualTo(LocalDateTime.of(
                2024, 11, 21, 9, 1, 1).toString());
        assertThat(result).extractingJsonPathStringValue("$.end").isEqualTo(LocalDateTime.of(
                2024, 11, 22, 9, 1, 1).toString());
        assertThat(result).extractingJsonPathNumberValue("$.itemId").isEqualTo(1);
    }
}