package ru.yandex.practicum.filmorate.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.constants.FilmErrorMessages;
import ru.yandex.practicum.filmorate.exception.NotFoundException;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.storage.FilmStorage;
import ru.yandex.practicum.filmorate.storage.UserStorage;

import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

@Service
public class FilmService {
    final FilmStorage inMemoryFilmStorage;
    final UserStorage inMemoryUserStorage;

    @Autowired
    public FilmService(FilmStorage inMemoryFilmStorage, UserStorage inMemoryUserStorage) {
        this.inMemoryFilmStorage = inMemoryFilmStorage;
        this.inMemoryUserStorage = inMemoryUserStorage;
    }

    public List<Film> getAll() {
        return inMemoryFilmStorage.getAll();
    }

    public Film create(Film film) {
        return inMemoryFilmStorage.add(film);
    }

    public Film update(Film film) throws NotFoundException {
        Film newFilm = inMemoryFilmStorage.update(film);

        if (Objects.isNull(newFilm)) {
            throw new NotFoundException(FilmErrorMessages.notFound);
        }

        return newFilm;
    }

    public Film getById(Long id) throws NotFoundException {
        return inMemoryFilmStorage.get(id);
    }

    public void addLike(Long filmId, Long userId) throws NotFoundException {
        Film film = inMemoryFilmStorage.get(filmId);
        inMemoryUserStorage.get(userId);

        Set<Long> likes = film.getLikes();
        likes.add(userId);
        film.setLikes(likes);
    }

    public void deleteLike(Long filmId, Long userId) throws NotFoundException {
        Film film = inMemoryFilmStorage.get(filmId);
        inMemoryUserStorage.get(userId);

        Set<Long> likes = film.getLikes();
        likes.remove(userId);
        film.setLikes(likes);
    }

    public List<Film> getMostLiked(Optional<Integer> count) {
        final int DEFAUILT_MOST_LIKED_COUNT = 10;

        return inMemoryFilmStorage.getAll()
                .stream()
                .sorted((s1, s2) -> s2.getLikes().size() - s1.getLikes().size())
                .limit(count.orElse(DEFAUILT_MOST_LIKED_COUNT))
                .collect(
                        Collectors.toList());
    }

}
