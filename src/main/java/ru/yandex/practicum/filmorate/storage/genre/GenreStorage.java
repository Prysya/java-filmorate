package ru.yandex.practicum.filmorate.storage.genre;

import ru.yandex.practicum.filmorate.model.Genre;

import java.util.List;

public interface GenreStorage {
    /**
     * Получение жанра по уникальному идентификатору
     *
     * @param id уникальный идентификатор
     * @return найденный жанр
     */
    Genre get(Long id);

    /**
     * Получение всех жанров
     *
     * @return список жанров
     */
    List<Genre> getAll();

    /**
     * Получение всех жанров из списка идентификаторов
     *
     * @param genres список жанров
     * @return список жанров
     */
    List<Genre> getAll(List<Genre> genres);

    /**
     * Получение списка жанров фильма
     *
     * @return список жанров
     */
    List<Genre> getFilmGenres(Long filmId);

    /**
     * Добавление жанров фильму
     *
     * @param filmId уникальный идентификатор фильма
     * @param genres список жанров
     */
    void setFilmGenres(Long filmId, List<Genre> genres);

    /**
     * Удаление жанров у фильма
     *
     * @param filmId уникальный идентификатор фильма
     */
    void deleteFilmGenres(Long filmId);
}