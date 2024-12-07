package ru.practicum.shareit.request.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import ru.practicum.shareit.request.model.ItemRequest;

import java.util.Collection;

public interface ItemRequestRepository extends JpaRepository<ItemRequest, Integer> {

    @Query(value = "SELECT ir FROM ItemRequest ir WHERE ir.user.id = ?1 ORDER BY ir.created DESC")
    Collection<ItemRequest> getRequestsForUser(int id);
}
