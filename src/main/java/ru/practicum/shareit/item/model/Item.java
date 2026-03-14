package ru.practicum.shareit.item.model;

import lombok.Data;
import ru.practicum.shareit.user.User;

/**
 * Модель вещи.
 */
@Data
public class Item {
    private Long id;
    private String name;
    private String description;
    private Boolean available; // доступна для аренды
    private User owner;        // владелец
    private Long requestId;    // ссылка на запрос (пока не используется)
}
