package ru.yandex.practicum.filmorate.controller;

import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;
import ru.yandex.practicum.filmorate.constants.FilmErrorMessages;
import ru.yandex.practicum.filmorate.exception.NotFoundException;
import ru.yandex.practicum.filmorate.model.Film;

import javax.validation.Valid;
import java.util.*;

@Slf4j
@RestController
@RequestMapping("/films")
public class FilmController {
    /**
     * Мапа фильмов
     */
    final Map<Integer, Film> films = new HashMap<>();

    @GetMapping
    public List<Film> getAll() {
        return new ArrayList<>(films.values());
    }

    @PostMapping
    public Film create(@Valid @RequestBody Film film) {
        film.setId(films.size() + 1);
        films.put(film.getId(), film);

        log.debug("Создан новый фильм: {}", film);

        return film;
    }

    @PutMapping
    public Film update(@Valid @RequestBody Film film) throws NotFoundException {
        boolean hasId = Objects.nonNull(film.getId());

        if (hasId) {
            if (films.containsKey(film.getId())) {
                films.put(film.getId(), film);

                log.debug("Обновлен фильм с id({}): {}", film.getId(), film);

                return film;
            }

            throw new NotFoundException(FilmErrorMessages.notFound);
        }


        return create(film);
    }
}
