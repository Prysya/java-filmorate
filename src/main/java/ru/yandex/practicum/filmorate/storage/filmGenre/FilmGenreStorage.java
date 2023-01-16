package ru.yandex.practicum.filmorate.storage.filmGenre;

import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.model.FilmGenre;

import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.Set;


public interface FilmGenreStorage {
    /**
     * Получение жанра по уникальному идентификатору
     *
     * @param id уникальный идентификатор
     * @return найденный жанр
     */
    Optional<FilmGenre> get(Long id);

    /**
     * Получение всех жанров
     *
     * @return список жанров
     */
    List<FilmGenre> getAll();

    /**
     * Получение списка жанров фильма
     *
     * @param filmId уникальный идентификатор фильма
     * @return список жанров
     */
    Set<FilmGenre> getFilmGenres(Long filmId);

    /**
     * Получение мапы жанров по фильму
     *
     * @param films список фильмов
     * @return мапа жанров по айди фильма
     */
    Map<Long, Set<FilmGenre>> getFilmGenres(List<Film> films);

    /**
     * Добавление жанров фильму
     *
     * @param filmId уникальный идентификатор фильма
     * @param genres список жанров
     */
    void setFilmGenres(Long filmId, Set<FilmGenre> genres);

    /**
     * Удаление жанров у фильма
     *
     * @param filmId уникальный идентификатор фильма
     */
    void deleteFilmGenres(Long filmId);
}
