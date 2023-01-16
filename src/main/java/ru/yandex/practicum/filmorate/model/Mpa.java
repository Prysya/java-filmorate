package ru.yandex.practicum.filmorate.model;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class Mpa {
    /**
     * Уникальный идентификатор
     */
    private final Long id;
    /**
     * Рейтинг ассоциации
     */
    private String name;
}
