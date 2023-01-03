package ru.yandex.practicum.filmorate.storage.film;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.constants.FilmErrorMessages;
import ru.yandex.practicum.filmorate.exception.NotFoundException;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.utils.NullValidator;

import java.util.*;

@Slf4j
@Component
public class InMemoryFilmStorage implements FilmStorage {
    /**
     * Мапа фильмов
     */
    private final Map<Long, Film> films = new HashMap<>();


    @Override
    public Film get(Long id) throws NotFoundException {
        if (films.containsKey(id)) {
            return films.get(id);
        }

        throw new NotFoundException(FilmErrorMessages.notFound);
    }

    @Override
    public Film add(Film film) {
        NullValidator.validate(film, FilmErrorMessages.badRequest);

        film.setId((long) (films.size() + 1));

        films.put(film.getId(), film);

        log.debug("Создан новый фильм: {}", film);

        return film;
    }

    @Override
    public Film update(Film film) {
        NullValidator.validate(film, FilmErrorMessages.badRequest);
        boolean hasId = Objects.nonNull(film.getId());

        if (hasId) {
            if (films.containsKey(film.getId())) {
                films.put(film.getId(), film);

                log.debug("Обновлен фильм с id({}): {}", film.getId(), film);

                return film;
            }

            log.debug("Фильм с id({}) не найден", film.getId());
            throw new NotFoundException(FilmErrorMessages.notFound);
        }


        return add(film);
    }

    @Override
    public List<Film> getAll() {
        return new ArrayList<>(films.values());
    }
}
