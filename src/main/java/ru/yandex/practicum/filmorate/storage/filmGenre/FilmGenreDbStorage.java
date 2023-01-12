package ru.yandex.practicum.filmorate.storage.filmGenre;

import lombok.extern.slf4j.Slf4j;
import org.springframework.dao.DuplicateKeyException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.exception.NotFoundException;
import ru.yandex.practicum.filmorate.model.Genre;
import ru.yandex.practicum.filmorate.utils.Mapper;

import java.sql.PreparedStatement;
import java.util.List;
import java.util.Objects;

@Slf4j
@Component
public class FilmGenreDbStorage implements FilmGenreStorage {
    private final String NOT_FOUND_MESSAGE = "Жанр не найден";
    private final JdbcTemplate jdbcTemplate;

    public FilmGenreDbStorage(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }


    @Override
    public Genre get(Long id) {
        String sqlQuery = "select * from FILM_GENRE WHERE FILM_GENRE_ID = ?";

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
        String sqlQuery = "select * from FILM_GENRE";

        return jdbcTemplate.query(sqlQuery, Mapper::mapRowToGenre);
    }

    @Override
    public List<Genre> getFilmGenres(Long filmId) {
        String sqlQuery = "SELECT FG.* " +
            "FROM FILM_GENRES FGS " +
            "JOIN FILM_GENRE FG on FG.FILM_GENRE_ID = FGS.FILM_GENRE_ID " +
            "WHERE FILM_ID = ? " +
            "ORDER BY FILM_GENRE_ID";

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

        String sqlQuery = "insert into FILM_GENRES(FILM_ID, FILM_GENRE_ID) " +
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
        String sqlQuery = "delete from FILM_GENRES " +
            "where FILM_ID = ?";

        jdbcTemplate.update(sqlQuery, filmId);
    }
}
