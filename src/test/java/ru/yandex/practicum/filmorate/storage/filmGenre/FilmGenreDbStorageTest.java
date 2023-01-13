package ru.yandex.practicum.filmorate.storage.filmGenre;

import lombok.RequiredArgsConstructor;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.DirtiesContext;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.model.FilmGenre;
import ru.yandex.practicum.filmorate.model.Mpa;
import ru.yandex.practicum.filmorate.storage.film.FilmDbStorage;

import java.time.LocalDate;
import java.util.*;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest(properties = {"spring.config.name=myapp-test-h2", "spring.datasource.url=jdbc:h2:mem:trxServiceStatus"})
@DirtiesContext(classMode = DirtiesContext.ClassMode.BEFORE_EACH_TEST_METHOD)
@AutoConfigureTestDatabase
@RequiredArgsConstructor(onConstructor_ = @Autowired)
class FilmGenreDbStorageTest {

    private final FilmGenreDbStorage genreStorage;
    private final FilmDbStorage filmStorage;

    private Film createBasicFilm() {
        return filmStorage.add(Film.builder()
            .name("Film")
            .duration(2)
            .releaseDate(LocalDate.now())
            .mpa(Mpa.builder().id(1L).build())
            .description("")
            .build()).get();
    }


    @Test
    void shouldReturnGenreById() {
        FilmGenre filmGenre = genreStorage.get(1L).get();

        assertEquals(filmGenre.getClass(), FilmGenre.class);
        assertTrue(Objects.nonNull(filmGenre.getId()));
        assertThat(filmGenre).hasFieldOrPropertyWithValue("id", 1L);
    }

    @Test
    void shouldReturnListOfGenres() {
        List<FilmGenre> filmGenreList =  genreStorage.getAll();

        assertFalse(filmGenreList.isEmpty());
        assertEquals(filmGenreList.get(0).getClass(), FilmGenre.class);
        assertTrue(Objects.nonNull(filmGenreList.get(0).getId()));
        assertThat(filmGenreList.get(0)).hasFieldOrPropertyWithValue("id", 1L);
    }

    @Test
    void shoulSetAndReturnFilmGenres() {
        Film film = createBasicFilm();
        genreStorage.setFilmGenres(film.getId(), Set.of(FilmGenre.builder().id(1L).build()));

        Set<FilmGenre> filmGenres = genreStorage.getFilmGenres(film.getId());
        assertEquals(filmGenres.stream().findFirst().get().getId(), 1L);

        genreStorage.setFilmGenres(film.getId(), Set.of(FilmGenre.builder().id(3L).build()));
        Set<FilmGenre> newFilmGenres = genreStorage.getFilmGenres(film.getId());
        assertEquals(newFilmGenres.stream().findFirst().get().getId(), 3L);

    }

    @Test
    void shouldDeleteFilmGenres() {
        Film film = createBasicFilm();
        genreStorage.setFilmGenres(film.getId(), Set.of(FilmGenre.builder().id(1L).build()));

        Set<FilmGenre> filmGenres = genreStorage.getFilmGenres(film.getId());
        assertEquals(filmGenres.size(), 1);

        genreStorage.deleteFilmGenres(film.getId());
        Set<FilmGenre> emptyFilmGenres = genreStorage.getFilmGenres(film.getId());
        assertTrue(emptyFilmGenres.isEmpty());
    }

    @Test
    void shouldReturnMapOfGenres() {
        List<Film> films = List.of(createBasicFilm(),createBasicFilm());
        films.forEach(film -> {
            genreStorage.setFilmGenres(film.getId(), Set.of(FilmGenre.builder().id(1L).build()));
        });

        Map<Long, Set<FilmGenre>> genres = genreStorage.getFilmGenres(films);
        assertEquals(genres.get(1L).size(), 1);
        assertEquals(genres.get(2L).size(), 1);
        assertEquals(genres.get(1L).getClass(), LinkedHashSet.class);
        assertEquals(genres.get(1L).stream().findFirst().get().getId(), 1L);
    }
}
