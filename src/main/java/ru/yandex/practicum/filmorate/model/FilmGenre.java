package ru.yandex.practicum.filmorate.model;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class FilmGenre {
    /**
     * Уникальный идентификатор
     */
    private final Long id;
    /**
     * Имя жанра
     */
    private String name;
}
