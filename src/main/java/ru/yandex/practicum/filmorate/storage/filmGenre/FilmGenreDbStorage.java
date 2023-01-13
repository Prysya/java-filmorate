package ru.yandex.practicum.filmorate.storage.filmGenre;

import lombok.extern.slf4j.Slf4j;
import org.springframework.dao.DuplicateKeyException;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.model.FilmGenre;
import ru.yandex.practicum.filmorate.utils.Mapper;

import java.sql.PreparedStatement;
import java.util.*;

@Slf4j
@Component
public class FilmGenreDbStorage implements FilmGenreStorage {
    private final JdbcTemplate jdbcTemplate;

    public FilmGenreDbStorage(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }


    @Override
    public Optional<FilmGenre> get(Long id) {
        String sqlQuery = "select * from FILM_GENRE WHERE FILM_GENRE_ID = ?";

        try {
            return Optional.ofNullable(jdbcTemplate.queryForObject(sqlQuery, Mapper::mapRowToGenre, id));
        } catch (EmptyResultDataAccessException e) {
            return Optional.empty();
        }
    }

    @Override
    public List<FilmGenre> getAll() {
        String sqlQuery = "select * from FILM_GENRE";

        return jdbcTemplate.query(sqlQuery, Mapper::mapRowToGenre);
    }

    @Override
    public Set<FilmGenre> getFilmGenres(Long filmId) {
        String sqlQuery = "SELECT FG.* " +
            "FROM FILM_GENRES FGS " +
            "JOIN FILM_GENRE FG on FG.FILM_GENRE_ID = FGS.FILM_GENRE_ID " +
            "WHERE FILM_ID = ? " +
            "ORDER BY FILM_GENRE_ID";

        return new LinkedHashSet<>(jdbcTemplate.query(sqlQuery, Mapper::mapRowToGenre, filmId));
    }

    @Override
    public Map<Long, Set<FilmGenre>> getFilmGenres(List<Film> films) {
        Map<Long, Set<FilmGenre>> genresById = new HashMap<>();

        String inSql = String.join(",", Collections.nCopies(films.size(), "?"));
        String sqlQuery = "SELECT FGS.*, FG.FILM_GENRE_NAME " +
            "FROM FILM_GENRES FGS " +
            "JOIN FILM_GENRE FG on FG.FILM_GENRE_ID = FGS.FILM_GENRE_ID " +
            "WHERE FILM_ID IN (%s)";

        jdbcTemplate.query(
            String.format(sqlQuery, inSql),
            (rs, rowNum) -> {
                Long filmId = rs.getLong("film_id");
                FilmGenre filmGenre = Mapper.mapRowToGenre(rs, rowNum);
                Set<FilmGenre> genres = genresById.getOrDefault(filmId, new LinkedHashSet<>());
                genres.add(filmGenre);

                return genresById.put(
                    filmId,
                    genres
                );
            },
            films.stream().map(film -> film.getId().toString()).toArray()
        );

        return genresById;
    }

    @Override
    public void setFilmGenres(Long filmId, Set<FilmGenre> genres) {
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
            jdbcTemplate.batchUpdate(sqlQuery, genres, 100, (PreparedStatement ps, FilmGenre genre) -> {
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
