package ru.practicum.shareit.item.repository;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.jdbc.core.JdbcTemplate;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.user.User;
import ru.practicum.shareit.user.repository.UserRepository;

import java.util.Collection;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@DataJpaTest
class ItemRepositoryTest {
    @Autowired
    ItemRepository itemRepository;
    @Autowired
    UserRepository userRepository;
    @Autowired
    JdbcTemplate jdbcTemplate;
    User user1;
    User user2;
    Item item1;
    Item item2;

    @BeforeEach
    void setUp() {
        jdbcTemplate.execute("ALTER TABLE users ALTER COLUMN id RESTART WITH 1");
        jdbcTemplate.execute("ALTER TABLE items ALTER COLUMN id RESTART WITH 1");

        user1 = new User(1, "userName1", "email1@mail.ru");
        user2 = new User(2, "userName2", "email2@mail.ru");
        userRepository.save(user1);
        userRepository.save(user2);

        item1 = new Item(1, "Item1", "Item1 description1", true, user1, 0);
        item2 = new Item(2, "Item2", "Item2 description2", true, user2, 1);
        itemRepository.save(item1);
        itemRepository.save(item2);
    }

    @Test
    void getItemForUser() {
        Item expectedItem = item1;
        assertEquals(expectedItem, itemRepository.getItemForUser(1, 1));
        assertNull(itemRepository.getItemForUser(1, 3));
    }

    @Test
    void findByOwner() {
        Collection<Item> expectedItem = List.of(item1);
        assertEquals(expectedItem, itemRepository.findByOwner(user1));
    }

    @Test
    void findByNameOrDescription() {
        Collection<Item> expectedItem = List.of(item1);
        assertEquals(expectedItem, itemRepository.findByNameOrDescription("item1"));
        assertEquals(expectedItem, itemRepository.findByNameOrDescription("description1"));

    }

    @Test
    void findByRequest() {
        Collection<Item> expectedItem = List.of(item2);
        assertEquals(expectedItem, itemRepository.findByRequest(user1.getId()));
    }
}