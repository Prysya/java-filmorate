package ru.yandex.practicum.filmorate.storage.film;

import lombok.extern.slf4j.Slf4j;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.constant.FilmErrorMessages;
import ru.yandex.practicum.filmorate.exception.NotFoundException;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.utils.Converter;
import ru.yandex.practicum.filmorate.utils.Mapper;

import java.sql.PreparedStatement;
import java.util.List;
import java.util.Objects;

@Slf4j
@Component
public class FilmDbStorage implements FilmStorage {
    private final JdbcTemplate jdbcTemplate;

    public FilmDbStorage(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    @Override
    public Film get(Long id) {
        String sqlQuery = "select * " +
            "from FILMS " +
            "where FILM_ID = ?";

        try {
            Film film = jdbcTemplate.queryForObject(sqlQuery, Mapper::mapRowToFilm, id);

            if (Objects.nonNull(film)) {
                log.info("Найден фильм: {} {}", film.getId(), film.getName());
                return film;
            }
        } catch (Exception ignored) {
        }

        log.info("Фильм с идентификатором {} не найден.", id);
        throw new NotFoundException(FilmErrorMessages.notFound);
    }

    @Override
    public Film add(Film film) {
        String sqlQuery = "insert into FILMS(name, description, release_date, duration, mpa_rating_id) " +
            "values (?, ?, ?, ?, ?)";
        KeyHolder keyHolder = new GeneratedKeyHolder();

        jdbcTemplate.update(connection -> {
            PreparedStatement stmt = connection.prepareStatement(sqlQuery, new String[] {"film_id"});
            stmt.setString(1, film.getName());
            stmt.setString(2, film.getDescription());
            stmt.setDate(3, Converter.convertToDateViaSqlDate(film.getReleaseDate()));
            stmt.setInt(4, film.getDuration());
            stmt.setLong(5, film.getMpa().getId());
            return stmt;
        }, keyHolder);


        Number key = keyHolder.getKey();
        if (Objects.nonNull(key)) {
            log.info("Айди созданного фильма: {}", key);

            return get(key.longValue());
        }


        log.info("Фильм не создан");
        return null;
    }

    @Override
    public List<Film> getAll() {
        String sqlQuery = "select * " +
            "from FILMS";

        return jdbcTemplate.query(sqlQuery, Mapper::mapRowToFilm);
    }

    @Override
    public Film update(Film film) {
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

        if (status > 0) {
            log.info("Фильм с id = {} успешно обновлен", film.getId());
            return get(film.getId());
        }

        throw new NotFoundException(FilmErrorMessages.notFound);
    }
}
