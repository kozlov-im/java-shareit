package ru.practicum.shareit.item.dto;

import lombok.RequiredArgsConstructor;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.json.JsonTest;
import org.springframework.boot.test.json.JacksonTester;
import org.springframework.boot.test.json.JsonContent;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.user.User;

import java.time.LocalDateTime;

import static org.assertj.core.api.Assertions.assertThat;

@JsonTest
@RequiredArgsConstructor(onConstructor_ = @Autowired)
class CommentDtoTest {
    private final JacksonTester<CommentDto> json;

    @Test
    void commentDtoTest() throws Exception {
        User user = new User(1, "userName", "email@mail.ru");
        Item item = new Item(1, "itemName", "itemDescription", true, user, 0);

        CommentDto commentDto = new CommentDto(
                1,
                "comment",
                item,
                "userName",
                LocalDateTime.of(2024, 11, 21, 9, 1, 1));

        JsonContent<CommentDto> result = json.write(commentDto);
        assertThat(result).extractingJsonPathNumberValue("$.id").isEqualTo(1);
        assertThat(result).extractingJsonPathStringValue("$.text").isEqualTo("comment");
        assertThat(result).extractingJsonPathNumberValue("$.item.id").isEqualTo(1);
        assertThat(result).extractingJsonPathStringValue("$.item.name").isEqualTo("itemName");
        assertThat(result).extractingJsonPathStringValue("$.item.description").isEqualTo("itemDescription");
        assertThat(result).extractingJsonPathBooleanValue("$.item.available").isEqualTo(true);
        assertThat(result).extractingJsonPathNumberValue("$.item.owner.id").isEqualTo(1);
        assertThat(result).extractingJsonPathStringValue("$.item.owner.name").isEqualTo("userName");
        assertThat(result).extractingJsonPathStringValue("$.item.owner.email").isEqualTo("email@mail.ru");
        assertThat(result).extractingJsonPathStringValue("$.created").isEqualTo(LocalDateTime.of(
                2024, 11, 21, 9, 1, 1).toString());

    }
}