package ru.yandex.practicum.filmorate.storage.likes;

import ru.yandex.practicum.filmorate.model.Film;

import java.util.List;

public interface LikesStorage {
    /**
     * Получение жанра по уникальному идентификатору
     *
     * @param filmId уникальный идентификатор пользователя
     * @return найденный пользователь
     */
    List<Long> get(Long filmId);

    /**
     * Получение жанра по уникальному идентификатору
     *
     * @param filmId уникальный идентификатор пользователя
     * @param userId уникальный идентификатор пользователя
     */
    void add(Long filmId, Long userId);

    /**
     * Получение жанра по уникальному идентификатору
     *
     * @param filmId уникальный идентификатор пользователя
     * @param userId уникальный идентификатор пользователя
     */
    void delete(Long filmId, Long userId);

    /**
     * Получение списка самый залайканых фильмов
     *
     * @param count количество фильмов
     * @return список фильмов
     */
    List<Film> getMostLikedFilms(int count);
}
