package ru.practicum.shareit.user.repository;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.jdbc.core.JdbcTemplate;
import ru.practicum.shareit.user.User;

import static org.junit.jupiter.api.Assertions.*;

@DataJpaTest
class UserRepositoryTest {

    @Autowired
    UserRepository repository;

    @Autowired
    JdbcTemplate jdbcTemplate;

    User user1;
    User user2;

    @BeforeEach
    void setUp() {
        user1 = new User("userName1", "email1@mail.ru");
        user2 = new User("userName2", "email2@mail.ru");
        repository.save(user1);
        repository.save(user2);
    }

    @AfterEach
    public void execute() {
        jdbcTemplate.execute("ALTER TABLE users ALTER COLUMN id RESTART WITH 1");
    }

    @Test
    void findByEmail_getResult() {
        User userExpected = new User(1, "userName1", "email1@mail.ru");
        User userResult = repository.findByEmail("email1@mail.ru");
        assertEquals(userExpected, userResult);
    }

    @Test
    void findByEmail_empty() {
        User userResult = repository.findByEmail("email@mail.ru");
        assertNull(userResult);
    }
}