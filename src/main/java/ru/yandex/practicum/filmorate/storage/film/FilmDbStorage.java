package ru.yandex.practicum.filmorate.storage.film;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.utils.Converter;
import ru.yandex.practicum.filmorate.utils.Mapper;

import java.sql.PreparedStatement;
import java.util.List;
import java.util.Objects;
import java.util.Optional;

@Slf4j
@Component
@RequiredArgsConstructor
public class FilmDbStorage implements FilmStorage {
    private final JdbcTemplate jdbcTemplate;

    @Override
    public Optional<Film> get(Long id) {
        String sqlQuery = "SELECT F.*, MR.MPA_RATING_NAME " +
            "FROM FILMS AS F " +
            "LEFT JOIN LIKES L on F.FILM_ID = L.FILM_ID " +
            "LEFT JOIN MPA_RATING MR on MR.MPA_RATING_ID = F.MPA_RATING_ID " +
            "WHERE F.FILM_ID = ?;";

        try {
            return Optional.ofNullable(jdbcTemplate.queryForObject(sqlQuery, Mapper::mapRowToFilm, id));
        } catch (EmptyResultDataAccessException e) {
            return Optional.empty();
        }
    }

    @Override
    public Optional<Film> add(Film film) {
        String sqlQuery = "insert into FILMS(name, description, release_date, duration, mpa_rating_id) " +
            "values (?, ?, ?, ?, ?)";
        KeyHolder keyHolder = new GeneratedKeyHolder();

        try {
            jdbcTemplate.update(connection -> {
                PreparedStatement stmt = connection.prepareStatement(sqlQuery, new String[] {"film_id"});
                stmt.setString(1, film.getName());
                stmt.setString(2, film.getDescription());
                stmt.setDate(3, Converter.convertToDateViaSqlDate(film.getReleaseDate()));
                stmt.setInt(4, film.getDuration());
                stmt.setLong(5, film.getMpa().getId());
                return stmt;
            }, keyHolder);

            film.setId(Objects.requireNonNull(keyHolder.getKey()).longValue());
            return Optional.of(film);
        } catch (NullPointerException e) {
            return Optional.empty();
        }
    }

    @Override
    public List<Film> getAll() {
        String sqlQuery = "SELECT F.*, MR.MPA_RATING_NAME " +
            "FROM FILMS AS F " +
            "LEFT JOIN LIKES L on F.FILM_ID = L.FILM_ID " +
            "LEFT JOIN MPA_RATING MR on MR.MPA_RATING_ID = F.MPA_RATING_ID";

        return jdbcTemplate.query(sqlQuery, Mapper::mapRowToFilm);
    }

    @Override
    public Optional<Film> update(Film film) {
        String sqlQuery = "update FILMS set " +
            "NAME = ?, DESCRIPTION = ?, RELEASE_DATE = ?, DURATION = ?, MPA_RATING_ID = ? " +
            "where FILM_ID = ?";

        int status = jdbcTemplate.update(sqlQuery,
            film.getName(),
            film.getDescription(),
            Converter.convertToDateViaSqlDate(film.getReleaseDate()),
            film.getDuration(),
            film.getMpa().getId(),
            film.getId()
        );

        return status > 0 ? Optional.of(film) : Optional.empty();
    }
}
