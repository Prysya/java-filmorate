package ru.yandex.practicum.filmorate.model;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import lombok.Builder;
import lombok.Data;
import lombok.extern.jackson.Jacksonized;
import org.springframework.format.annotation.DateTimeFormat;
import ru.yandex.practicum.filmorate.annotation.validator.ReleaseDateValidation;
import ru.yandex.practicum.filmorate.constant.FilmErrorMessages;

import javax.validation.constraints.Min;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.time.LocalDate;
import java.util.LinkedHashSet;
import java.util.Set;

@Data
@Jacksonized
@Builder
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
    private Long id;
    /**
     * Рейтинг Ассоциации кинокомпаний
     */
    @NotNull(message = FilmErrorMessages.mpaIsEmpty)
    private Mpa mpa;
    /**
     * Жанры
     */
    @JsonDeserialize(as = LinkedHashSet.class)
    private Set<FilmGenre> genres;
}
