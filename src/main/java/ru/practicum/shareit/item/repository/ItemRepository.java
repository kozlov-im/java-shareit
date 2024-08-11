package ru.practicum.shareit.item.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.user.User;

import java.util.Collection;

public interface ItemRepository extends JpaRepository<Item, Integer> {


    @Query(value = "SELECT i FROM Item i WHERE i.id = ?1 AND i.owner.id = ?2")
    Item getItemForUser(int itemId, int userId);

    Collection<Item> findByOwner(User user);

    @Query(value = "SELECT i FROM Item i WHERE (LOWER(i.name) LIKE %?1% OR LOWER(i.description) LIKE %?1%) AND i.available = TRUE")
    Collection<Item> findByNameOrDescription(String text);
}
