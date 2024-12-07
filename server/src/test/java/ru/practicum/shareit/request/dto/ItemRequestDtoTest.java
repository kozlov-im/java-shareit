package ru.practicum.shareit.request.dto;

import lombok.RequiredArgsConstructor;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.json.JsonTest;
import org.springframework.boot.test.json.JacksonTester;
import org.springframework.boot.test.json.JsonContent;
import ru.practicum.shareit.user.User;

import java.time.LocalDate;

import static org.assertj.core.api.Assertions.assertThat;

@JsonTest
@RequiredArgsConstructor(onConstructor_ = @Autowired)
class ItemRequestDtoTest {
    private final JacksonTester<ItemRequestDto> json;

    @Test
    void itemRequestDtoTest() throws Exception {
        User user = new User(1, "userName", "email@mail.ru");
        ItemRequestDto itemRequestDto = new ItemRequestDto(
                1,
                "description",
                user,
                LocalDate.of(2024, 11, 20));

        JsonContent<ItemRequestDto> result = json.write(itemRequestDto);

        assertThat(result).extractingJsonPathNumberValue("$.id").isEqualTo(1);
        assertThat(result).extractingJsonPathStringValue("$.description").isEqualTo("description");
        assertThat(result).extractingJsonPathNumberValue("$.user.id").isEqualTo(1);
        assertThat(result).extractingJsonPathStringValue("$.user.name").isEqualTo("userName");
        assertThat(result).extractingJsonPathStringValue("$.user.email").isEqualTo("email@mail.ru");
        assertThat(result).extractingJsonPathStringValue("$.created").isEqualTo(LocalDate.of(
                2024, 11, 20).toString());

    }

}