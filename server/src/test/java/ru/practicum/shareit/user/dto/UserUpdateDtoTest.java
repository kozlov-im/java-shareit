package ru.practicum.shareit.user.dto;

import lombok.RequiredArgsConstructor;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.json.JsonTest;
import org.springframework.boot.test.json.JacksonTester;
import org.springframework.boot.test.json.JsonContent;

import java.io.IOException;

import static org.assertj.core.api.Assertions.assertThat;
@JsonTest
@RequiredArgsConstructor(onConstructor_ = @Autowired)
class UserUpdateDtoTest {
    private final JacksonTester<UserUpdateDto> json;

    @Test
    void userUpdateDtoTest() throws IOException {
        UserUpdateDto userUpdateDto = new UserUpdateDto(
                "userName",
                "email@mail.ru"
        );
        JsonContent<UserUpdateDto> result = json.write(userUpdateDto);
        assertThat(result).extractingJsonPathStringValue("$.name").isEqualTo("userName");
        assertThat(result).extractingJsonPathStringValue("$.email").isEqualTo("email@mail.ru");
    }

}