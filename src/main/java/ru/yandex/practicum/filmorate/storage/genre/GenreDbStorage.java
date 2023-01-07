package ru.yandex.practicum.filmorate.storage.genre;

import lombok.extern.slf4j.Slf4j;
import org.springframework.dao.DuplicateKeyException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.exception.NotFoundException;
import ru.yandex.practicum.filmorate.model.Genre;
import ru.yandex.practicum.filmorate.utils.Mapper;

import java.sql.PreparedStatement;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

@Slf4j
@Component
public class GenreDbStorage implements GenreStorage {
    private final String NOT_FOUND_MESSAGE = "Жанр не найден";
    private final JdbcTemplate jdbcTemplate;

    public GenreDbStorage(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }


    @Override
    public Genre get(Long id) {
        String sqlQuery = "select * from GENRE WHERE GENRE_ID = ?";

        try {
            Genre genre = jdbcTemplate.queryForObject(sqlQuery, Mapper::mapRowToGenre, id);

            if (Objects.nonNull(genre)) {
                log.info("Найден жанр: {} {}", genre.getId(), genre.getName());
                return genre;
            }
        } catch (Exception ignored) {
        }

        log.info("Жанр с идентификатором {} не найден.", id);
        throw new NotFoundException(NOT_FOUND_MESSAGE);
    }

    @Override
    public List<Genre> getAll() {
        String sqlQuery = "select * from GENRE";

        return jdbcTemplate.query(sqlQuery, Mapper::mapRowToGenre);
    }

    @Override
    public List<Genre> getFilmGenres(Long filmId) {
        String sqlQuery = "SELECT G.* " +
            "FROM GENRES GS " +
            "JOIN GENRE G on G.GENRE_ID = GS.GENRE_ID " +
            "WHERE FILM_ID = ? " +
            "ORDER BY GENRE_ID";

        return jdbcTemplate.query(sqlQuery, Mapper::mapRowToGenre, filmId);
    }

    @Override
    public void setFilmGenres(Long filmId, List<Genre> genres) {
        if (Objects.isNull(genres)) {
            return;
        }

        deleteFilmGenres(filmId);

        if (genres.isEmpty()) {
            return;
        }

        String sqlQuery = "insert into GENRES(FILM_ID, GENRE_ID) " +
            "values ( ?, ? )";

        try {
            jdbcTemplate.batchUpdate(sqlQuery, genres, 100, (PreparedStatement ps, Genre genre) -> {
                ps.setLong(1, filmId);
                ps.setLong(2, genre.getId());
            });
        } catch (DuplicateKeyException ignored) {
        }
    }

    @Override
    public void deleteFilmGenres(Long filmId) {
        String sqlQuery = "delete from GENRES " +
            "where FILM_ID = ?";

        jdbcTemplate.update(sqlQuery, filmId);
    }
}
