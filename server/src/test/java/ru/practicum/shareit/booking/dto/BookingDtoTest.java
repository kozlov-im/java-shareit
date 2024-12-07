package ru.practicum.shareit.booking.dto;

import lombok.RequiredArgsConstructor;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.json.JsonTest;
import org.springframework.boot.test.json.JacksonTester;
import org.springframework.boot.test.json.JsonContent;
import ru.practicum.shareit.booking.Status;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.user.User;

import java.time.LocalDateTime;

import static org.assertj.core.api.Assertions.assertThat;

@JsonTest
@RequiredArgsConstructor(onConstructor_ = @Autowired)
class BookingDtoTest {
    private final JacksonTester<BookingDto> json;

    @Test
    void bookingDtoTest() throws Exception {
        User user = new User(1, "userName", "email@mail.ru");
        Item item = new Item(1, "itemName", "itemDescription", true, user, 0);

        BookingDto bookingDto = new BookingDto(
                1,
                LocalDateTime.of(2024, 11, 20, 9, 1, 1),
                LocalDateTime.of(2024, 11, 21, 9, 1, 1),
                item,
                user,
                Status.WAITING);

        JsonContent<BookingDto> result = json.write(bookingDto);

        assertThat(result).extractingJsonPathNumberValue("$.id").isEqualTo(1);
        assertThat(result).extractingJsonPathStringValue("$.start").isEqualTo(LocalDateTime.of(
                2024, 11, 20, 9, 1, 1).toString());
        assertThat(result).extractingJsonPathStringValue("$.end").isEqualTo(LocalDateTime.of(
                2024, 11, 21, 9, 1, 1).toString());
        assertThat(result).extractingJsonPathNumberValue("$.item.id").isEqualTo(1);
        assertThat(result).extractingJsonPathStringValue("$.item.name").isEqualTo("itemName");
        assertThat(result).extractingJsonPathStringValue("$.item.description").isEqualTo("itemDescription");
        assertThat(result).extractingJsonPathBooleanValue("$.item.available").isEqualTo(true);
        assertThat(result).extractingJsonPathNumberValue("$.item.owner.id").isEqualTo(1);
        assertThat(result).extractingJsonPathStringValue("$.item.owner.name").isEqualTo("userName");
        assertThat(result).extractingJsonPathStringValue("$.item.owner.email").isEqualTo("email@mail.ru");
        assertThat(result).extractingJsonPathStringValue("$.status").isEqualTo(Status.WAITING.toString());

    }

}