package ru.yandex.practicum.filmorate.storage.likes;

import lombok.extern.slf4j.Slf4j;
import org.springframework.dao.DuplicateKeyException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.utils.Mapper;

import java.util.List;

@Slf4j
@Component
public class LikesDbStorage implements LikesStorage {
    private final JdbcTemplate jdbcTemplate;

    public LikesDbStorage(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    @Override
    public List<Long> get(Long filmId) {
        String sqlQuery = "SELECT U.USER_ID " +
            "FROM LIKES " +
            "JOIN USERS U on U.USER_ID = LIKES.USER_ID " +
            "WHERE FILM_ID = ?";

        return jdbcTemplate.query(sqlQuery, (rs, rowNum) -> rs.getLong("user_id"), filmId);
    }

    @Override
    public void add(Long filmId, Long userId) {
        String sqlQuery = "insert into LIKES(film_id, user_id) " +
            "values ( ?, ? )";

        try {
            jdbcTemplate.update(sqlQuery, filmId, userId);
        } catch (DuplicateKeyException ignored) {
            log.warn("Пользователь с id = {} уже ставил лайк фильму с id = {}", userId, filmId);
        }

        log.info("Пользователь с id = {} поставил лайк фильму с id = {}", userId, filmId);
    }

    @Override
    public void delete(Long filmId, Long userId) {
        String sqlQuery = "delete from LIKES " +
            "where FILM_ID = ? and USER_ID = ?";

        jdbcTemplate.update(sqlQuery, filmId, userId);
    }

    @Override
    public List<Film> getMostLikedFilms(int count) {
        String sqlQuery = "SELECT F.* " +
            "FROM FILMS AS F " +
            "LEFT JOIN LIKES L on F.FILM_ID = L.FILM_ID " +
            "GROUP BY F.NAME " +
            "ORDER BY COUNT(L.USER_ID) DESC " +
            "LIMIT ?";

        return jdbcTemplate.query(sqlQuery, Mapper::mapRowToFilm, count);
    }
}