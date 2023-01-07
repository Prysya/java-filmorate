package ru.yandex.practicum.filmorate.model;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class Genre {
    /**
     * Уникальный идентификатор
     */
    private final Long id;
    /**
     * Имя жанра
     */
    private String name;
}
