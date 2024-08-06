package ru.practicum.shareit.item.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import ru.practicum.shareit.item.model.Comment;

import java.util.Collection;

public interface CommentRepository extends JpaRepository<Comment, Integer> {

    @Query(value = "SELECT c FROM Comment c WHERE c.item.id = ?1")
    Collection<Comment> getCommentsForItem(int itemId);
}
