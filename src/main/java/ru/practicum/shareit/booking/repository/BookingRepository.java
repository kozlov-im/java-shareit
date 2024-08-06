package ru.practicum.shareit.booking.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import ru.practicum.shareit.booking.model.Booking;

import java.util.Collection;

public interface BookingRepository extends JpaRepository<Booking, Integer> {
    @Query(value = "SELECT b FROM Booking b JOIN b.item i WHERE i.owner.id = ?1 AND b.id = ?2")
    Booking getBookingByOwner(int ownerId, int bookingId);

    @Query(value = "SELECT b FROM Booking b JOIN b.booker bk WHERE bk.id = ?1 AND b.id = ?2")
    Booking getBookingByBooker(int bookerId, int bookingId);

    @Query(value = "SELECT b FROM Booking b JOIN b.item i JOIN i.owner JOIN b.booker WHERE i.owner.id = ?1 or b.booker.id = ?1 ORDER BY b.id DESC")
    Collection<Booking> getAllForUser(int userId);

    @Query(value = "SELECT b FROM Booking b JOIN b.item i JOIN i.owner JOIN b.booker WHERE (i.owner.id = ?1 or b.booker.id = ?1) AND b.start < CURRENT_TIMESTAMP AND b.end > CURRENT_TIMESTAMP ORDER BY b.start DESC")
    Collection<Booking> getAllForUserWithStateCurrent(int userId);

    @Query(value = "SELECT b FROM Booking b JOIN b.item i JOIN i.owner JOIN b.booker WHERE (i.owner.id = ?1 or b.booker.id = ?1) AND b.end < CURRENT_TIMESTAMP ORDER BY b.end DESC")
    Collection<Booking> getAllForUserWithStatePast(int userId);

    @Query(value = "SELECT b FROM Booking b JOIN b.item i JOIN i.owner JOIN b.booker WHERE (i.owner.id = ?1 or b.booker.id = ?1) AND b.start > CURRENT_TIMESTAMP ORDER BY b.start DESC")
    Collection<Booking> getAllForUserWithStateFuture(int userId);

    @Query(value = "SELECT b FROM Booking b JOIN b.item i JOIN i.owner JOIN b.booker WHERE (i.owner.id = ?1 or b.booker.id = ?1) AND b.status = ?2 ORDER BY b.start DESC")
    public Collection<Booking> getAllForUserWithState(int userId, Enum status);

    @Query(value = "SELECT b FROM Booking b JOIN b.item i WHERE i.id = ?1 AND b.status = 'APPROVED' AND b.start < CURRENT_TIMESTAMP AND b.end > CURRENT_TIMESTAMP")
    Booking getCurrentApprovedBookingForItem(int itemId);

    @Query(value = "SELECT b FROM Booking b JOIN b.item i WHERE i.id = ?1 AND b.status = 'APPROVED' AND b.start > CURRENT_TIMESTAMP AND b.end > CURRENT_TIMESTAMP ORDER BY b.start ASC")
    Collection<Booking> getFutureApprovedBookingForItem(int itemId);

    @Query(value = "SELECT b FROM Booking b JOIN b.item i  WHERE i.owner.id = ?1 ORDER BY b.start DESC")
    Collection<Booking> getBookingForOwner(int ownerId);

    @Query(value = "SELECT b FROM Booking b JOIN b.item i  WHERE i.owner.id = ?1 AND b.start < CURRENT_TIMESTAMP AND b.end > CURRENT_TIMESTAMP  ORDER BY b.start DESC")
    Collection<Booking> getBookingForOwnerWithStateCurrent(int ownerId);

    @Query(value = "SELECT b FROM Booking b JOIN b.item i  WHERE i.owner.id = ?1 AND b.end < CURRENT_TIMESTAMP ORDER BY b.start DESC")
    Collection<Booking> getBookingForOwnerWithStatePast(int ownerId);

    @Query(value = "SELECT b FROM Booking b JOIN b.item i  WHERE i.owner.id = ?1 AND b.start > CURRENT_TIMESTAMP ORDER BY b.start DESC")
    Collection<Booking> getBookingForOwnerWithStateFuture(int ownerId);

    @Query(value = "SELECT b FROM Booking b JOIN b.item i  WHERE i.owner.id = ?1 AND b.status = ?2 ORDER BY b.start DESC")
    Collection<Booking> getBookingForOwnerWithState(int ownerId, Enum status);

    @Query(value = "SELECT b FROM Booking b JOIN b.item i JOIN i.owner WHERE i.id = ?1 AND i.owner.id = ?2")
    Collection<Booking> getBookingForItem(int itemId, int ownerId);

    @Query(value = "SELECT b FROM Booking b WHERE b.booker.id = ?1 AND b.item.id = ?2 AND b.status = 'APPROVED' and b.end < CURRENT_TIMESTAMP")
    Collection<Booking> getApprovedBookingForBookerPast(int bookerId, int itemId);
}

