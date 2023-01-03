package ru.yandex.practicum.filmorate.storage.film;

import ru.yandex.practicum.filmorate.model.Film;

import java.util.List;

public interface FilmStorage {
    /**
     * Получение фильма по уникальному идентификатору
     *
     * @param id уникальный идентификатор
     * @return найденный фильм
     */
    Film get(Long id);

    /**
     * Добавление фильма в список
     *
     * @param item новый фильм
     * @return созданный фильм
     */
    Film add(Film item);

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
    Film update(Film film);
}
