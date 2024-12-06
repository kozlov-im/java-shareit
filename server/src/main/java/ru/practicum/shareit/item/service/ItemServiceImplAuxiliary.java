package ru.practicum.shareit.item.service;

import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import ru.practicum.shareit.booking.dto.BookingDto;
import ru.practicum.shareit.booking.mapper.BookingMapper;
import ru.practicum.shareit.booking.model.Booking;
import ru.practicum.shareit.item.dto.CommentDto;
import ru.practicum.shareit.item.dto.ItemDtoBooking;
import ru.practicum.shareit.item.mapper.ItemMapper;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.item.repository.ItemRepository;

import java.time.LocalDateTime;
import java.util.*;
import java.util.stream.Collectors;

@Service
@AllArgsConstructor
public class ItemServiceImplAuxiliary {
    private final ItemRepository itemRepository;

    public ItemDtoBooking createBookingForItem(int itemId, Collection<Booking> itemBookings, Collection<CommentDto> commentsDto) {
        LocalDateTime currentTime = LocalDateTime.now().withNano(0);
        Collection<Booking> lastBookings = itemBookings.stream().filter(i -> i.getEnd().isBefore(currentTime))
                .collect(Collectors.toList());

        List lastBookingsList = new ArrayList<>(lastBookings);
        Collections.sort(lastBookingsList, new Comparator<Booking>() {
            public int compare(Booking b1, Booking b2) {
                return b1.getStart().compareTo(b2.getStart());
            }
        });


        Collection<Booking> nextBookings = itemBookings.stream().filter(i -> i.getEnd().isAfter(currentTime))
                .collect(Collectors.toList());

        List nextBookingsList = new ArrayList<>(nextBookings);

        Collections.sort(nextBookingsList, new Comparator<Booking>() {
            public int compare(Booking b1, Booking b2) {
                return b1.getStart().compareTo(b2.getStart());
            }
        });

        BookingDto lastBookingDto;
        BookingDto nextBookingDto;
        if (lastBookingsList.size() == 0) {
            lastBookingDto = null;
        } else {
            lastBookingDto = BookingMapper.toBookingDto((Booking) lastBookingsList.get(lastBookingsList.size() - 1));
        }

        if (nextBookingsList.size() == 0) {
            nextBookingDto = null;
        } else {
            nextBookingDto = BookingMapper.toBookingDto((Booking) nextBookingsList.get(0));
        }

        Item item = itemRepository.getReferenceById(itemId);
        return ItemMapper.toItemDtoBooking(item, lastBookingDto, nextBookingDto, commentsDto);
    }
}
