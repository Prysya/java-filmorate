package ru.yandex.practicum.filmorate.storage.likes;

import ru.yandex.practicum.filmorate.model.Film;

import java.util.List;

public interface LikesStorage {
    /**
     * Получение списка идентификаторов лайкнувших пользователей по уникальному идентификатору фильма
     *
     * @param filmId уникальный идентификатор фильма
     * @return список идентификаторов пользователей
     */
    List<Long> get(Long filmId);

    /**
     * Запись лайка в БД
     *
     * @param filmId уникальный идентификатор фильма
     * @param userId уникальный идентификатор пользователя
     */
    void add(Long filmId, Long userId);

    /**
     * Удаление лайка из БД
     *
     * @param filmId уникальный идентификатор фильма
     * @param userId уникальный идентификатор пользователя
     */
    void delete(Long filmId, Long userId);

    /**
     * Получение списка фильмов с наибольшим количеством лайков
     *
     * @param count количество фильмов
     * @return список фильмов
     */
    List<Film> getMostLikedFilms(int count);
}
