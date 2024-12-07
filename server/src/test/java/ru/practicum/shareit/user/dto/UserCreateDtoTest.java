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
class UserCreateDtoTest {
    private final JacksonTester<UserCreateDto> json;

    @Test
    void userCreateDtoTest() throws IOException {
        UserCreateDto userCreateDto = new UserCreateDto(
                "userName",
                "email@mail.ru"
        );
        JsonContent<UserCreateDto> result = json.write(userCreateDto);
        assertThat(result).extractingJsonPathStringValue("$.name").isEqualTo("userName");
        assertThat(result).extractingJsonPathStringValue("$.email").isEqualTo("email@mail.ru");
    }
}