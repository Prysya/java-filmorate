package ru.yandex.practicum.filmorate.model;

import org.junit.jupiter.api.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import javax.validation.*;
import java.time.LocalDate;
import java.util.Calendar;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.*;

@RunWith(SpringRunner.class)
@SpringBootTest(webEnvironment= SpringBootTest.WebEnvironment.RANDOM_PORT)
class FilmTest {
    private final Validator validator = Validation.buildDefaultValidatorFactory().getValidator();

    @Test
    void shouldNotValidateIfNameIsNull() {
        Film film = new Film(null, "", LocalDate.of(2000, Calendar.OCTOBER, 10), 1);

        assertFalse(validator.validate(film).isEmpty());
    }

    @Test
    void shouldNotValidateIfNameIsBlank() {
        Film film = new Film("", "", LocalDate.of(2000, Calendar.OCTOBER, 10), 1);

        Set<ConstraintViolation<Film>> violations = validator.validate(film);

        assertFalse(violations.isEmpty());
    }

    @Test
    void shouldCreateUserWithFilledName() {
        Film film = new Film("Name", "", LocalDate.of(2000, Calendar.OCTOBER, 10), 1);

        assertTrue(validator.validate(film).isEmpty());
    }

    @Test
    void shouldNotValidateIfDescriptionLengthIs201() {
        Film film = new Film("Name", " ".repeat(201), LocalDate.of(2000, Calendar.OCTOBER, 10), 1);

        assertFalse(validator.validate(film).isEmpty());
    }

    @Test
    void shouldCreateFilmIfDescriptionLengthLowerOrEquals200() {
        Film film = new Film("Name", " ".repeat(200), LocalDate.of(2000, Calendar.OCTOBER, 10), 1);
        assertTrue(validator.validate(film).isEmpty());

        Film film1 = new Film("Name", " ".repeat(199), LocalDate.of(2000, Calendar.OCTOBER, 10), 1);
        assertTrue(validator.validate(film1).isEmpty());

        Film film2 = new Film("Name", " ", LocalDate.of(2000, Calendar.OCTOBER, 10), 1);
        assertTrue(validator.validate(film2).isEmpty());
    }

    @Test
    void shouldCreateFilmIfDescriptionIsNullOrBlank() {
        Film film = new Film("Name", null, LocalDate.of(2000, Calendar.OCTOBER, 10), 1);
        assertTrue(validator.validate(film).isEmpty());

        Film film1 = new Film("Name", "", LocalDate.of(2000, Calendar.OCTOBER, 10), 1);
        assertTrue(validator.validate(film1).isEmpty());
    }

    @Test
    void shouldNotValidateIfReleaseDateIsBeforeOrEquals_28_12_1895() {
        Film film = new Film("Name", "", LocalDate.of(1895, Calendar.DECEMBER, 28), 1);
        assertFalse(validator.validate(film).isEmpty());

        Film film1 = new Film("Name", "", LocalDate.of(1894, Calendar.DECEMBER, 28), 1);
        assertFalse(validator.validate(film1).isEmpty());
    }

    @Test
    void shouldNotValidateIfReleaseDateIsNull() {
        assertThrows(ValidationException.class, () -> {
            Film film = new Film("Name", "", null, 1);
            assertFalse(validator.validate(film).isEmpty());
        });
    }

    @Test
    void shouldCreateFilmIfDescriptionIsLaterThen_28_12_1895() {
        Film film = new Film("Name", "", LocalDate.of(1895, Calendar.DECEMBER, 29), 1);
        assertTrue(validator.validate(film).isEmpty());

        Film film1 = new Film("Name", "", LocalDate.of(2000, Calendar.OCTOBER, 10), 1);
        assertTrue(validator.validate(film1).isEmpty());
    }

    @Test
    void shouldNotValidateIfDurationIsNull() {
        Film film = new Film("Name", "", LocalDate.of(2000, Calendar.DECEMBER, 28), null);
        assertFalse(validator.validate(film).isEmpty());
    }

    @Test
    void shouldNotValidateIfDurationIsZeroOrLower() {
        Film film = new Film("Name", "", LocalDate.of(2000, Calendar.DECEMBER, 28), 0);
        assertFalse(validator.validate(film).isEmpty());

        Film film1 = new Film("Name", "", LocalDate.of(2000, Calendar.DECEMBER, 28), -1);
        assertFalse(validator.validate(film1).isEmpty());
    }

    @Test
    void shouldCreateFilmIfDurationIsOneOrHigher() {
        Film film = new Film("Name", "", LocalDate.of(1895, Calendar.DECEMBER, 29), 1);
        assertTrue(validator.validate(film).isEmpty());

        Film film1 = new Film("Name", "", LocalDate.of(2000, Calendar.OCTOBER, 10), 100);
        assertTrue(validator.validate(film1).isEmpty());
    }
}