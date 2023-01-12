package ru.yandex.practicum.filmorate.storage.filmGenre;

import lombok.RequiredArgsConstructor;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.DirtiesContext;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.model.Genre;
import ru.yandex.practicum.filmorate.model.Mpa;
import ru.yandex.practicum.filmorate.storage.film.FilmDbStorage;

import java.time.LocalDate;
import java.util.List;
import java.util.Objects;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest(properties = {"spring.config.name=myapp-test-h2", "spring.datasource.url=jdbc:h2:mem:trxServiceStatus"})
@DirtiesContext(classMode = DirtiesContext.ClassMode.BEFORE_EACH_TEST_METHOD)
@AutoConfigureTestDatabase
@RequiredArgsConstructor(onConstructor_ = @Autowired)
class GenreDbStorageTest {

    private final FilmGenreDbStorage genreStorage;
    private final FilmDbStorage filmStorage;

    private Film createBasicFilm() {
        return filmStorage.add(Film.builder()
            .name("Film")
            .duration(2)
            .releaseDate(LocalDate.now())
            .mpa(Mpa.builder().id(1L).build())
            .description("")
            .build());
    }


    @Test
    void shouldReturnGenreById() {
        Genre genre = genreStorage.get(1L);

        assertEquals(genre.getClass(), Genre.class);
        assertTrue(Objects.nonNull(genre.getId()));
        assertThat(genre).hasFieldOrPropertyWithValue("id", 1L);
    }

    @Test
    void shouldReturnListOfGenres() {
        List<Genre> genreList =  genreStorage.getAll();

        assertFalse(genreList.isEmpty());
        assertEquals(genreList.get(0).getClass(), Genre.class);
        assertTrue(Objects.nonNull(genreList.get(0).getId()));
        assertThat(genreList.get(0)).hasFieldOrPropertyWithValue("id", 1L);
    }

    @Test
    void shoulSetAndReturnFilmGenres() {
        Film film = createBasicFilm();
        genreStorage.setFilmGenres(film.getId(), List.of(Genre.builder().id(1L).build()));

        List<Genre> genres = genreStorage.getFilmGenres(film.getId());
        assertEquals(genres.get(0).getId(), 1L);

        genreStorage.setFilmGenres(film.getId(), List.of(Genre.builder().id(3L).build()));
        List<Genre> newGenres = genreStorage.getFilmGenres(film.getId());
        assertEquals(newGenres.get(0).getId(), 3L);

    }

    @Test
    void shouldDeleteFilmGenres() {
        Film film = createBasicFilm();
        genreStorage.setFilmGenres(film.getId(), List.of(Genre.builder().id(1L).build()));

        List<Genre> genres = genreStorage.getFilmGenres(film.getId());
        assertEquals(genres.size(), 1);

        genreStorage.deleteFilmGenres(film.getId());
        List<Genre> emptyGenres = genreStorage.getFilmGenres(film.getId());
        assertTrue(emptyGenres.isEmpty());
    }
}
