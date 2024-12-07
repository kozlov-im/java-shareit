package ru.practicum.shareit.item.dto;

import lombok.RequiredArgsConstructor;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.json.JsonTest;
import org.springframework.boot.test.json.JacksonTester;
import org.springframework.boot.test.json.JsonContent;
import ru.practicum.shareit.user.User;

import static org.assertj.core.api.Assertions.assertThat;

@JsonTest
@RequiredArgsConstructor(onConstructor_ = @Autowired)
class ItemCreateDtoTest {
    private final JacksonTester<ItemCreateDto> json;

    @Test
    void itemCreateDtoTest() throws Exception {
        User user1 = new User(1, "user1Name", "email1@mail.ru");
        ItemCreateDto itemCreateDto = new ItemCreateDto(
                "itemName",
                "itemDescription",
                true,
                user1,
                0);

        JsonContent<ItemCreateDto> result = json.write(itemCreateDto);

        assertThat(result).extractingJsonPathStringValue("$.name").isEqualTo("itemName");
        assertThat(result).extractingJsonPathStringValue("$.description").isEqualTo("itemDescription");
        assertThat(result).extractingJsonPathBooleanValue("$.available").isEqualTo(true);
        assertThat(result).extractingJsonPathNumberValue("$.owner.id").isEqualTo(1);
        assertThat(result).extractingJsonPathStringValue("$.owner.name").isEqualTo("user1Name");
        assertThat(result).extractingJsonPathStringValue("$.owner.email").isEqualTo("email1@mail.ru");

    }

}