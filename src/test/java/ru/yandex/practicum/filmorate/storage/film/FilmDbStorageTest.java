package ru.yandex.practicum.filmorate.storage.film;

import lombok.RequiredArgsConstructor;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.DirtiesContext;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.model.Mpa;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.LinkedHashSet;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.junit.jupiter.api.Assertions.assertEquals;

@SpringBootTest(properties = {"spring.config.name=myapp-test-h2", "spring.datasource.url=jdbc:h2:mem:trxServiceStatus"})
@DirtiesContext(classMode = DirtiesContext.ClassMode.BEFORE_EACH_TEST_METHOD)
@AutoConfigureTestDatabase
@RequiredArgsConstructor(onConstructor_ = @Autowired)
class FilmDbStorageTest {
    private final FilmDbStorage filmStorage;

    private Film createBasicFilm() {
        return filmStorage.add(Film.builder()
            .name("Film")
            .duration(2)
            .releaseDate(LocalDate.now())
            .mpa(Mpa.builder().id(1L).name("G").build())
            .description("")
                .genres(new LinkedHashSet<>())
            .build()).get();
    }

    @Test
    void shouldGetAndAddFilm() {
        Film newFilm = createBasicFilm();

        assertThat(newFilm).hasFieldOrPropertyWithValue("id", 1L);

        assertEquals(newFilm, filmStorage.get(newFilm.getId()).get());
    }

    @Test
    void shouldReturnListOfFilms() {
        final int FILMS_COUNT = 3;

        for (int i = 0; i < FILMS_COUNT; i += 1) {
            createBasicFilm();
        }

        assertEquals(filmStorage.getAll().size(), FILMS_COUNT);
    }

    @Test
    void shouldUpdateFilm() {
        Film film = createBasicFilm();

        Film newFilm = Film.builder()
            .name("Film1")
            .duration(3)
            .releaseDate(LocalDate.now())
            .mpa(film.getMpa())
            .description("123")
            .id(film.getId())
            .genres(film.getGenres())
            .build();

        filmStorage.update(newFilm);

        assertEquals(newFilm, filmStorage.get(film.getId()).get());
    }
}