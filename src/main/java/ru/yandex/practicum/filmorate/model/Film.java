package ru.yandex.practicum.filmorate.model;

import lombok.Data;
import org.springframework.format.annotation.DateTimeFormat;
import ru.yandex.practicum.filmorate.annotation.validator.ReleaseDateValidation;
import ru.yandex.practicum.filmorate.constants.FilmErrorMessages;

import javax.validation.constraints.Min;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.time.LocalDate;

@Data
public class Film {
    /**
     * Уникальный идентификатор
     */
    private Integer id = null;
    /**
     * Название
     */
    @NotNull(message = FilmErrorMessages.name)
    @NotBlank(message = FilmErrorMessages.name)
    private final String name;
    /**
     * Описание
     */
    @Size(max = 200, message = FilmErrorMessages.description)
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
}
