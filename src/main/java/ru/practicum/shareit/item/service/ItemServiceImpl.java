package ru.practicum.shareit.item.service;

import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import ru.practicum.shareit.booking.dto.BookingDto;
import ru.practicum.shareit.booking.mapper.BookingMapper;
import ru.practicum.shareit.booking.model.Booking;
import ru.practicum.shareit.booking.repository.BookingRepository;
import ru.practicum.shareit.exception.BadRequestException;
import ru.practicum.shareit.exception.InternalServerErrorException;
import ru.practicum.shareit.exception.NotFoundException;
import ru.practicum.shareit.item.dto.*;
import ru.practicum.shareit.item.mapper.CommentMapper;
import ru.practicum.shareit.item.mapper.ItemMapper;
import ru.practicum.shareit.item.model.Comment;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.item.repository.CommentRepository;
import ru.practicum.shareit.item.repository.ItemRepository;
import ru.practicum.shareit.user.service.UserService;

import java.time.LocalDateTime;
import java.util.*;
import java.util.stream.Collectors;

@Service
@AllArgsConstructor
public class ItemServiceImpl implements ItemService {
    private final ItemRepository itemRepository;
    private final UserService userService;
    private final BookingRepository bookingRepository;
    private final CommentRepository commentRepository;

    @Override
    public Item createItem(int userId, ItemCreateDto itemCreateDto) {
        Item item = ItemMapper.toItemModel(itemCreateDto);
        userService.checkUserExist(userId);
        item.setOwner(userService.getUserById(userId));
        return itemRepository.save(item);
    }

    @Override
    public Item updateItem(int userId, int itemId, ItemCreateDto itemCreateDto) {
        Item itemForUpdate = ItemMapper.toItemModel(itemCreateDto);
        userService.checkUserExist(userId);
        checkItemExist(itemId);
        Item item = checkItemForUser(userId, itemId);

        if (String.valueOf(itemForUpdate.getName()) != "null") {
            item.setName(itemForUpdate.getName());
        }
        if (String.valueOf(itemForUpdate.getDescription()) != "null") {
            item.setDescription(itemForUpdate.getDescription());
        }
        if (String.valueOf(itemForUpdate.getAvailable()) != "null") {
            item.setAvailable(itemForUpdate.getAvailable());
        }
        if (String.valueOf(itemForUpdate.getRequest()) != "null") {
            item.setRequest(itemForUpdate.getRequest());
        }
        return itemRepository.save(item);
    }


    @Override
    public Item getItemById(int itemId) {
        checkItemExist(itemId);
        return itemRepository.getReferenceById(itemId);
    }

    @Override
    public ItemDtoBooking getItemByIdAndUserId(int userId, int itemId) {
        Collection<Booking> itemBookings = bookingRepository.getBookingForItem(itemId, userId);
        Collection<Comment> comments = commentRepository.getCommentsForItem(itemId);
        Collection<CommentDto> commentsDto = new ArrayList<>();
        for (Comment comment : comments) {
            commentsDto.add(CommentMapper.toCommentDto(comment));
        }
        if (getItemById(itemId).getOwner().getId() != userId || itemBookings.size() == 0) {
            return ItemMapper.toItemDtoBooking(getItemById(itemId), null, null, commentsDto);
        }
        return createBookingForItem(itemId, itemBookings, commentsDto);
    }

    @Override
    public Collection<ItemDtoBooking> getItemsForUser(int userId) {
        userService.checkUserExist(userId);
        Collection<Item> items = itemRepository.getItemsForUser(userId);
        Collection<ItemDtoBooking> itemDtoBookings = new ArrayList<>();
        for (Item item : items) {
            Collection<Comment> comments = commentRepository.getCommentsForItem(item.getId());
            Collection<CommentDto> commentsDto = new ArrayList<>();
            for (Comment comment : comments) {
                commentsDto.add(CommentMapper.toCommentDto(comment));
            }
            ItemDtoBooking itemDtoBooking = createBookingForItem(item.getId(),
                    bookingRepository.getBookingForItem(item.getId(), userId), commentsDto
            );
            itemDtoBookings.add(itemDtoBooking);
        }
        return itemDtoBookings;
    }

    @Override
    public Collection<Item> searchItems(String text) {
        if (text.isEmpty()) {
            return new ArrayList<>();
        } else {
            return itemRepository.findByNameOrDescription(text.toLowerCase());
        }
    }

    @Override
    public void checkItemExist(int itemId) {
        if (!itemRepository.findById(itemId).isPresent()) {
            throw new NotFoundException("itemId = " + itemId + " isn't found");
        }
    }

    @Override
    public void checkItemAvailable(int itemId) {
        checkItemExist(itemId);
        if (itemRepository.getReferenceById(itemId).getAvailable() == false) {
            throw new InternalServerErrorException("itemId = " + itemId + " isn't available");
        }
    }

    @Override
    public CommentDto addComment(int userId, int itemId, CommentCreateDto commentDto) {
        userService.checkUserExist(userId);
        checkItemExist(itemId);
        if (bookingRepository.getApprovedBookingForBookerPast(userId, itemId).isEmpty()) {
            throw new BadRequestException("bookerId = " + userId + " doesn't have any past approved status for itemId = " + itemId);
        }
        Comment comment = CommentMapper.toCommentModel(commentDto,
                itemRepository.getReferenceById(itemId),
                userService.getUserById(userId));
        commentRepository.save(comment);
        return CommentMapper.toCommentDto(comment);
    }

    public Item checkItemForUser(int userId, int itemId) {
        Item item = itemRepository.getItemForUser(itemId, userId);
        System.out.println(item);
        if (item == null) {
            throw new NotFoundException("userId = " + userId + " isn't owner for itemId = " + itemId);
        }
        return item;
    }

    public ItemDtoBooking createBookingForItem(int itemId, Collection<Booking> itemBookings, Collection<CommentDto> commentsDto) {
        LocalDateTime currentTime = LocalDateTime.now();
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
