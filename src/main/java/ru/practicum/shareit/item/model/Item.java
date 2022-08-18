package ru.practicum.shareit.item.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.RequiredArgsConstructor;
import ru.practicum.shareit.booking.model.Booking;
import ru.practicum.shareit.user.model.User;

import javax.persistence.*;

/**
 * Объект вещи
 */
@Data
@Table(name = "items")
@Entity
@AllArgsConstructor
@RequiredArgsConstructor
public class Item {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;
    private String name;
    private String description;
    private String available;
    @ManyToOne
    @JoinColumn(name = "owner_id")
    private User user;
//    @ManyToOne
//    @JoinColumn(name = "booking_id")
//    private Booking booking;
}
