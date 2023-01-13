package ru.yandex.practicum.filmorate.model;

import org.junit.jupiter.api.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import javax.validation.Validation;
import javax.validation.ValidationException;
import javax.validation.Validator;
import java.time.LocalDate;
import java.time.Month;
import java.util.ArrayList;
import java.util.LinkedHashSet;

import static org.junit.jupiter.api.Assertions.*;

@RunWith(SpringRunner.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
class FilmTest {
    private final Validator validator = Validation.buildDefaultValidatorFactory().getValidator();

    private Film.FilmBuilder createBasicFilmBuilder() {
        return Film.builder()
            .name("name")
            .duration(1)
            .releaseDate(LocalDate.of(2000, Month.OCTOBER, 10))
            .genres(new LinkedHashSet<>())
            .mpa(Mpa.builder().id(1L).build());
    }

    @Test
    void shouldNotValidateIfNameIsNull() {
        Film film = createBasicFilmBuilder()
            .name(null)
            .build();

        assertFalse(validator.validate(film).isEmpty());
    }

    @Test
    void shouldNotValidateIfNameIsBlank() {
        Film film = createBasicFilmBuilder()
            .name("")
            .build();

        assertFalse(validator.validate(film).isEmpty());
    }

    @Test
    void shouldCreateUserWithFilledName() {
        Film film = createBasicFilmBuilder()
            .name("Name")
            .build();

        assertTrue(validator.validate(film).isEmpty());
    }

    @Test
    void shouldNotValidateIfDescriptionLengthIs201() {
        Film film = createBasicFilmBuilder()
            .description(" ".repeat(Film.MAX_DESCRIPTION_SIZE + 1))
            .build();

        assertFalse(validator.validate(film).isEmpty());
    }

    @Test
    void shouldCreateFilmIfDescriptionLengthLowerOrEquals_MAX_DESCRIPTION_SIZE() {
        assertTrue(validator.validate(
            createBasicFilmBuilder()
                .description(" ".repeat(Film.MAX_DESCRIPTION_SIZE))
                .build()
        ).isEmpty());

        assertTrue(validator.validate(
            createBasicFilmBuilder()
                .description(" ".repeat(Film.MAX_DESCRIPTION_SIZE - 1))
                .build()
        ).isEmpty());

        assertTrue(validator.validate(
            createBasicFilmBuilder()
                .description(" ")
                .build()
        ).isEmpty());
    }

    @Test
    void shouldCreateFilmIfDescriptionIsNullOrBlank() {
        assertTrue(validator.validate(
            createBasicFilmBuilder()
                .description(null)
                .build()
        ).isEmpty());

        assertTrue(validator.validate(
            createBasicFilmBuilder()
                .description("")
                .build()
        ).isEmpty());
    }

    @Test
    void shouldNotValidateIfReleaseDateIsBeforeThen_28_12_1895() {
        assertFalse(validator.validate(
            createBasicFilmBuilder()
                .releaseDate(LocalDate.of(1895, Month.DECEMBER, 27))
                .build()
        ).isEmpty());

        assertFalse(validator.validate(
            createBasicFilmBuilder()
                .releaseDate(LocalDate.of(1894, Month.DECEMBER, 28))
                .build()
        ).isEmpty());
    }

    @Test
    void shouldNotValidateIfReleaseDateIsNull() {
        assertThrows(ValidationException.class, () -> assertFalse(validator.validate(
            createBasicFilmBuilder()
                .releaseDate(null)
                .build()
        ).isEmpty()));
    }

    @Test
    void shouldCreateFilmIfReleaseDateIsLaterOrEquals_28_12_1895() {
        Film film = createBasicFilmBuilder()
            .releaseDate(LocalDate.of(1895, Month.DECEMBER, 29))
            .build();

        System.out.println(film);

        assertTrue(validator.validate(
            createBasicFilmBuilder()
                .releaseDate(LocalDate.of(1895, Month.DECEMBER, 29))
                .build()
        ).isEmpty());

        assertTrue(validator.validate(
            createBasicFilmBuilder()
                .releaseDate(LocalDate.of(1895, Month.DECEMBER, 28))
                .build()
        ).isEmpty());

        assertTrue(validator.validate(
            createBasicFilmBuilder()
                .releaseDate(LocalDate.of(2000, Month.OCTOBER, 10))
                .build()
        ).isEmpty());
    }

    @Test
    void shouldNotValidateIfDurationIsNull() {
        assertFalse(validator.validate(
            createBasicFilmBuilder()
                .duration(null)
                .build()
        ).isEmpty());
    }

    @Test
    void shouldNotValidateIfDurationIsZeroOrLower() {
        assertFalse(validator.validate(
            createBasicFilmBuilder()
                .duration(0)
                .build()
        ).isEmpty());

        assertFalse(validator.validate(
            createBasicFilmBuilder()
                .duration(-1)
                .build()
        ).isEmpty());
    }

    @Test
    void shouldCreateFilmIfDurationIsOneOrHigher() {
        assertTrue(validator.validate(
            createBasicFilmBuilder()
                .duration(1)
                .build()
        ).isEmpty());

        assertTrue(validator.validate(
            createBasicFilmBuilder()
                .duration(1000)
                .build()
        ).isEmpty());
    }

    @Test
    void shouldNotCreateFilmIfMpaIsNull() {
        assertFalse(validator.validate(
            createBasicFilmBuilder()
                .mpa(null)
                .build()
        ).isEmpty());
    }
}