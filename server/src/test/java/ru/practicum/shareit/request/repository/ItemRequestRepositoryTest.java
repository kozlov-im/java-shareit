package ru.practicum.shareit.request.repository;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import ru.practicum.shareit.request.model.ItemRequest;
import ru.practicum.shareit.user.User;
import ru.practicum.shareit.user.repository.UserRepository;

import java.time.LocalDateTime;
import java.util.Collection;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@DataJpaTest
class ItemRequestRepositoryTest {
    @Autowired
    ItemRequestRepository itemRequestRepository;

    @Autowired
    UserRepository userRepository;

    @Test
    void getRequestsForUser() {
        User user1 = new User(1,"userName1", "email1@mail.ru");
        User user2 = new User(2,"userName2", "email2@mail.ru");
        userRepository.save(user1);
        userRepository.save(user2);
        ItemRequest itemRequest1 = new ItemRequest("request1 description", user1, LocalDateTime.of(2024,11,20,0,0));
        ItemRequest itemRequest2 = new ItemRequest("request2 description", user2, LocalDateTime.of(2024,11,21,0,0));
        itemRequestRepository.save(itemRequest1);
        itemRequestRepository.save(itemRequest2);
        Collection<ItemRequest> expectedRequest = List.of(itemRequest1);
        assertEquals(expectedRequest, itemRequestRepository.getRequestsForUser(user1.getId()));

    }
}