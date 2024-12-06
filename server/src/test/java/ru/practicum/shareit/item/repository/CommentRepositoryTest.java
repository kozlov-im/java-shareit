package ru.practicum.shareit.item.repository;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.jdbc.core.JdbcTemplate;
import ru.practicum.shareit.item.model.Comment;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.user.User;
import ru.practicum.shareit.user.repository.UserRepository;

import java.time.LocalDateTime;
import java.util.Collection;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@DataJpaTest
class CommentRepositoryTest {
    @Autowired
    CommentRepository commentRepository;

    @Autowired
    UserRepository userRepository;

    @Autowired
    ItemRepository itemRepository;

    @Autowired
    JdbcTemplate jdbcTemplate;

    @Test
    void findByItem() {
        jdbcTemplate.execute("ALTER TABLE users ALTER COLUMN id RESTART WITH 1");
        jdbcTemplate.execute("ALTER TABLE items ALTER COLUMN id RESTART WITH 1");
        User user1 = new User(1,"userName1", "email1@mail.ru");
        User user2 = new User(2,"userName2", "email2@mail.ru");
        userRepository.save(user1);
        userRepository.save(user2);

        Item item1 = new Item(1, "Item1", "Item1 description1", true, user1,  0);
        Item item2 = new Item(2, "Item2", "Item2 description2", true, user2,  0);
        itemRepository.save(item1);
        itemRepository.save(item2);

        Comment comment1 = new Comment("Comment1", item1, user1, LocalDateTime.of(2024, 11, 21, 0, 0));
        Comment comment2 = new Comment("Comment2", item2, user2, LocalDateTime.of(2024, 11, 22, 0, 0));
        commentRepository.save(comment1);
        commentRepository.save(comment2);

        Collection<Comment> expectedComment = List.of(comment1);
        assertEquals(expectedComment, commentRepository.findByItem(item1));
    }
}