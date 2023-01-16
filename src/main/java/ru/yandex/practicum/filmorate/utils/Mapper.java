package ru.yandex.practicum.filmorate.utils;

import lombok.experimental.UtilityClass;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.model.FilmGenre;
import ru.yandex.practicum.filmorate.model.Mpa;
import ru.yandex.practicum.filmorate.model.User;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.LinkedHashSet;

@UtilityClass
public class Mapper {
    public static User mapRowToUser(ResultSet resultSet, int rowNum) throws SQLException {
        return User.builder()
            .id(resultSet.getLong("user_id"))
            .email(resultSet.getString("email"))
            .login(resultSet.getString("login"))
            .name(resultSet.getString("name"))
            .birthday(resultSet.getDate("birthday").toLocalDate())
            .build();
    }

    public static Film mapRowToFilm(ResultSet resultSet, int rowNum) throws SQLException {
        return Film.builder()
            .id(resultSet.getLong("film_id"))
            .name(resultSet.getString("name"))
            .description(resultSet.getString("description"))
            .releaseDate(resultSet.getDate("release_date").toLocalDate())
            .duration(resultSet.getInt("duration"))
            .genres(new LinkedHashSet<>())
            .mpa(mapRowToMpa(resultSet, rowNum))
            .build();
    }

    public static FilmGenre mapRowToGenre(ResultSet resultSet, int rowNum) throws SQLException {
        return FilmGenre.builder()
            .id(resultSet.getLong("film_genre_id"))
            .name(resultSet.getString("film_genre_name"))
            .build();
    }

    public static Mpa mapRowToMpa(ResultSet resultSet, int rowNum) throws SQLException {
        return Mpa.builder()
            .id(resultSet.getLong("mpa_rating_id"))
            .name(resultSet.getString("mpa_rating_name"))
            .build();
    }
}
