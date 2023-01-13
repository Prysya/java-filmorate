package ru.yandex.practicum.filmorate.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.exception.NotFoundException;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.model.FilmGenre;
import ru.yandex.practicum.filmorate.storage.filmGenre.FilmGenreStorage;

import java.util.List;
import java.util.Map;
import java.util.Set;

@Service
@RequiredArgsConstructor
public class FilmGenreService {
    private final FilmGenreStorage filmGenreStorage;

    public List<FilmGenre> getAll() {
        return filmGenreStorage.getAll();
    }

    public FilmGenre getById(Long id) {
        return filmGenreStorage.get(id).orElseThrow(() -> new NotFoundException("Жанр не найден"));
    }

    public Map<Long, Set<FilmGenre>> getFilmGenres(List<Film> films) {
        return filmGenreStorage.getFilmGenres(films);
    }
}
