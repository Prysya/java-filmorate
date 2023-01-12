package ru.yandex.practicum.filmorate.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.constant.FilmErrorMessages;
import ru.yandex.practicum.filmorate.exception.NotFoundException;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.model.Genre;
import ru.yandex.practicum.filmorate.model.Mpa;
import ru.yandex.practicum.filmorate.storage.film.FilmStorage;
import ru.yandex.practicum.filmorate.storage.filmGenre.FilmGenreStorage;
import ru.yandex.practicum.filmorate.storage.likes.LikesStorage;
import ru.yandex.practicum.filmorate.storage.mpa.MpaStorage;
import ru.yandex.practicum.filmorate.storage.user.UserStorage;

import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class FilmService {
    private final FilmStorage filmStorage;
    private final UserStorage userStorage;
    private final LikesStorage likesStorage;
    private final MpaStorage mpaStorage;
    private final FilmGenreStorage filmGenreStorage;

    public List<Film> getAll() {
        return setMpaAndGenres(filmStorage.getAll());
    }

    public Film create(Film film) {
        Film newFilm = filmStorage.add(film);
        setGenres(newFilm, film.getGenres());
        return setMpaAndGenres(newFilm);
    }

    public Film update(Film film) throws NotFoundException {
        Film newFilm = filmStorage.update(film);

        if (Objects.isNull(newFilm)) {
            throw new NotFoundException(FilmErrorMessages.notFound);
        }

        setGenres(newFilm, film.getGenres());

        return setMpaAndGenres(newFilm);
    }

    public Film getById(Long id) throws NotFoundException {
        return setMpaAndGenres(filmStorage.get(id));
    }

    public void addLike(Long filmId, Long userId) throws NotFoundException {
        filmStorage.get(filmId);
        userStorage.get(userId);

        likesStorage.add(filmId, userId);
    }

    public void deleteLike(Long filmId, Long userId) throws NotFoundException {
        filmStorage.get(filmId);
        userStorage.get(userId);

        likesStorage.delete(filmId, userId);
    }

    public List<Film> getMostLiked(Optional<Integer> count) {
        final int DEFAUILT_MOST_LIKED_COUNT = 10;

        return setMpaAndGenres(likesStorage.getMostLikedFilms(count.orElse(DEFAUILT_MOST_LIKED_COUNT)));
    }

    private List<Film> setMpaAndGenres(List<Film> films) {
        return films.stream().peek(this::setMpaAndGenres).collect(Collectors.toList());
    }

    private Film setMpaAndGenres(Film film) {
        if (Objects.nonNull(film.getGenres())) {
            try {
                List<Genre> genres = filmGenreStorage.getFilmGenres(film.getId());
                film.setGenres(genres);
            } catch (Exception ignored) {
            }
        }

        if (Objects.nonNull(film.getMpa())) {
            try {
                Mpa mpa = mpaStorage.get(film.getMpa().getId());
                film.setMpa(mpa);
            } catch (Exception ignored) {
            }
        }

        return film;
    }

    private void setGenres(Film film, List<Genre> genres) {
        if (Objects.nonNull(film) && Objects.nonNull(genres)) {
            filmGenreStorage.setFilmGenres(film.getId(), genres);
        }
    }
}
