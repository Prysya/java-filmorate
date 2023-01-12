package ru.yandex.practicum.filmorate.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.exception.NotFoundException;
import ru.yandex.practicum.filmorate.model.Genre;
import ru.yandex.practicum.filmorate.storage.filmGenre.FilmGenreStorage;

import java.util.List;

@Service
@RequiredArgsConstructor
public class FilmGenreService {
    private final FilmGenreStorage filmGenreStorage;

    public List<Genre> getAll() {
        return filmGenreStorage.getAll();
    }

    public Genre getById(Long id) throws NotFoundException {
        return filmGenreStorage.get(id);
    }
}
