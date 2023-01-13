package ru.yandex.practicum.filmorate.storage.film;

import ru.yandex.practicum.filmorate.model.Film;

import java.util.List;
import java.util.Optional;

public interface FilmStorage {
    /**
     * Получение фильма по уникальному идентификатору
     *
     * @param id уникальный идентификатор
     * @return найденный фильм
     */
    Optional<Film> get(Long id);

    /**
     * Добавление фильма в список
     *
     * @param film новый фильм
     * @return созданный фильм
     */
    Optional<Film> add(Film film);

    /**
     * Получение листа всех фильмов
     *
     * @return список {@link Film}
     */
    List<Film> getAll();

    /**
     * Обновление фильма
     *
     * @param film фильм
     * @return обновленный фильм
     */
    Optional<Film> update(Film film);
}
