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

import static org.junit.jupiter.api.Assertions.*;

@RunWith(SpringRunner.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
class FilmTest {
    private final Validator validator = Validation.buildDefaultValidatorFactory().getValidator();

    @Test
    void shouldNotValidateIfNameIsNull() {
        Film film = new Film(null, "", LocalDate.of(2000, Month.OCTOBER, 10), 1);

        assertFalse(validator.validate(film).isEmpty());
    }

    @Test
    void shouldNotValidateIfNameIsBlank() {
        Film film = new Film("", "", LocalDate.of(2000, Month.OCTOBER, 10), 1);

        assertFalse(validator.validate(film).isEmpty());
    }

    @Test
    void shouldCreateUserWithFilledName() {
        Film film = new Film("Name", "", LocalDate.of(2000, Month.OCTOBER, 10), 1);

        assertTrue(validator.validate(film).isEmpty());
    }

    @Test
    void shouldNotValidateIfDescriptionLengthIs201() {
        Film film = new Film(
                "Name",
                " ".repeat(Film.MAX_DESCRIPTION_SIZE + 1),
                LocalDate.of(2000, Month.OCTOBER, 10),
                1
        );

        assertFalse(validator.validate(film).isEmpty());
    }

    @Test
    void shouldCreateFilmIfDescriptionLengthLowerOrEquals_MAX_DESCRIPTION_SIZE() {
        assertTrue(validator.validate(new Film(
                "Name",
                " ".repeat(Film.MAX_DESCRIPTION_SIZE),
                LocalDate.of(2000, Month.OCTOBER, 10),
                1
        )).isEmpty());
        assertTrue(validator.validate(new Film(
                "Name",
                " ".repeat(Film.MAX_DESCRIPTION_SIZE - 1),
                LocalDate.of(2000, Month.OCTOBER, 10),
                1
        )).isEmpty());
        assertTrue(validator.validate(new Film("Name", " ", LocalDate.of(2000, Month.OCTOBER, 10), 1)).isEmpty());
    }

    @Test
    void shouldCreateFilmIfDescriptionIsNullOrBlank() {
        Film film = new Film("Name", null, LocalDate.of(2000, Month.OCTOBER, 10), 1);
        assertTrue(validator.validate(film).isEmpty());

        Film film1 = new Film("Name", "", LocalDate.of(2000, Month.OCTOBER, 10), 1);
        assertTrue(validator.validate(film1).isEmpty());
    }

    @Test
    void shouldNotValidateIfReleaseDateIsBeforeThen_28_12_1895() {
        assertFalse(validator.validate(new Film("Name", "", LocalDate.of(1895, Month.DECEMBER, 27), 1)).isEmpty());
        assertFalse(validator.validate(new Film("Name", "", LocalDate.of(1894, Month.DECEMBER, 28), 1)).isEmpty());
    }

    @Test
    void shouldNotValidateIfReleaseDateIsNull() {
        assertThrows(ValidationException.class, () -> {
            Film film = new Film("Name", "", null, 1);
            assertFalse(validator.validate(film).isEmpty());
        });
    }

    @Test
    void shouldCreateFilmIfDescriptionIsLaterOrEquals_28_12_1895() {
        assertTrue(validator.validate(new Film("Name", "", LocalDate.of(1895, Month.DECEMBER, 29), 1)).isEmpty());
        assertTrue(validator.validate(new Film("Name", "", LocalDate.of(1895, Month.DECEMBER, 28), 1)).isEmpty());
        assertTrue(validator.validate(new Film("Name", "", LocalDate.of(2000, Month.OCTOBER, 10), 1)).isEmpty());
    }

    @Test
    void shouldNotValidateIfDurationIsNull() {
        Film film = new Film("Name", "", LocalDate.of(2000, Month.DECEMBER, 28), null);
        assertFalse(validator.validate(film).isEmpty());
    }

    @Test
    void shouldNotValidateIfDurationIsZeroOrLower() {
        Film film = new Film("Name", "", LocalDate.of(2000, Month.DECEMBER, 28), 0);
        assertFalse(validator.validate(film).isEmpty());

        Film film1 = new Film("Name", "", LocalDate.of(2000, Month.DECEMBER, 28), -1);
        assertFalse(validator.validate(film1).isEmpty());
    }

    @Test
    void shouldCreateFilmIfDurationIsOneOrHigher() {
        Film film = new Film("Name", "", LocalDate.of(1895, Month.DECEMBER, 29), 1);
        assertTrue(validator.validate(film).isEmpty());

        Film film1 = new Film("Name", "", LocalDate.of(2000, Month.OCTOBER, 10), 100);
        assertTrue(validator.validate(film1).isEmpty());
    }
}