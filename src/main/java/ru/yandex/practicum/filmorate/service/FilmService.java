package ru.yandex.practicum.filmorate.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.constant.FilmErrorMessages;
import ru.yandex.practicum.filmorate.constant.UserErrorMessages;
import ru.yandex.practicum.filmorate.exception.NotFoundException;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.model.FilmGenre;
import ru.yandex.practicum.filmorate.storage.film.FilmStorage;
import ru.yandex.practicum.filmorate.storage.filmGenre.FilmGenreStorage;
import ru.yandex.practicum.filmorate.storage.likes.LikesStorage;
import ru.yandex.practicum.filmorate.storage.user.UserStorage;

import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Set;
import java.util.stream.Collectors;

@Slf4j
@Service
@RequiredArgsConstructor
public class FilmService {
    private final FilmStorage filmStorage;
    private final UserStorage userStorage;
    private final LikesStorage likesStorage;
    private final FilmGenreStorage filmGenreStorage;

    public List<Film> getAll() {
        return setGenres(filmStorage.getAll());
    }

    public Film create(Film film) {
        Film createdFilm = filmStorage.add(film);
        filmGenreStorage.setFilmGenres(film.getId(), film.getGenres());

        return setGenres(createdFilm);
    }

    public Film update(Film film) {
        Film updatedFilm = filmStorage.update(film);
        filmGenreStorage.setFilmGenres(film.getId(), film.getGenres());

        return setGenres(updatedFilm);
    }

    public Film getById(Long id) {
        Film film =
            filmStorage.get(id).orElseThrow(() -> new NotFoundException(String.format(FilmErrorMessages.notFound, id)));

        return setGenres(film);
    }

    public void addLike(Long filmId, Long userId) {
        checkFilmAndUserExists(filmId, userId);
        likesStorage.add(filmId, userId);
    }

    public void deleteLike(Long filmId, Long userId) {
        checkFilmAndUserExists(filmId, userId);

        likesStorage.delete(filmId, userId);
    }

    public List<Film> getMostLiked(Integer count) {
        return setGenres(likesStorage.getMostLikedFilms(count));
    }

    private List<Film> setGenres(List<Film> films) {
        Map<Long, Set<FilmGenre>> genres = filmGenreStorage.getFilmGenres(films);

        return films.stream().peek(film -> {
            if (genres.containsKey(film.getId())) {
                film.setGenres(genres.get(film.getId()));
            }
        }).collect(Collectors.toList());
    }

    private Film setGenres(Film film) {
        if (Objects.nonNull(film.getGenres())) {
            try {
                Set<FilmGenre> filmGenres = filmGenreStorage.getFilmGenres(film.getId());
                film.setGenres(filmGenres);
            } catch (Exception ignored) {
            }
        }
        return film;
    }

    private void checkFilmAndUserExists(Long filmId, Long userId) {
        filmStorage.get(filmId)
            .orElseThrow(() -> new NotFoundException(String.format(FilmErrorMessages.notFound, filmId)));
        userStorage.get(userId)
            .orElseThrow(() -> new NotFoundException(String.format(UserErrorMessages.notFound, userId)));
    }
}
