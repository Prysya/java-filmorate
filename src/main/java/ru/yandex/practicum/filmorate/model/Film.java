package ru.yandex.practicum.filmorate.model;

import lombok.Data;
import org.springframework.format.annotation.DateTimeFormat;
import ru.yandex.practicum.filmorate.annotation.validator.ReleaseDateValidation;
import ru.yandex.practicum.filmorate.constant.FilmErrorMessages;

import javax.validation.constraints.Min;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.time.LocalDate;
import java.util.HashSet;
import java.util.Set;

@Data
public class Film {
    /**
     * Константа с максимальной длинной описания
     */
    public static final int MAX_DESCRIPTION_SIZE = 200;
    /**
     * Название
     */
    @NotNull(message = FilmErrorMessages.name)
    @NotBlank(message = FilmErrorMessages.name)
    private final String name;
    /**
     * Описание
     */
    @Size(max = MAX_DESCRIPTION_SIZE, message = FilmErrorMessages.description)
    private final String description;
    /**
     * Дата релиза
     */
    @DateTimeFormat
    @ReleaseDateValidation
    private final LocalDate releaseDate;
    /**
     * Продолжительность
     */
    @NotNull(message = FilmErrorMessages.duration)
    @Min(value = 1, message = FilmErrorMessages.duration)
    private final Integer duration;
    /**
     * Уникальный идентификатор
     */
    private Long id = null;
    /**
     * Список уникальных идентификаторов пользователей которые поставили лайк
     */
    private Set<Long> likes = new HashSet<>();
    /**
     * Список жанров
     */
    private Set<String> genre = new HashSet<>();
    /**
     * Рейтинг Ассоциации кинокомпаний
     */
    @NotNull(message = FilmErrorMessages.mpaIsEmpty)
    private String mpaRating;
}
