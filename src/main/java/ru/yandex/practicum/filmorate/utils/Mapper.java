package ru.yandex.practicum.filmorate.utils;

import lombok.experimental.UtilityClass;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.model.Genre;
import ru.yandex.practicum.filmorate.model.Mpa;
import ru.yandex.practicum.filmorate.model.User;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

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
            .genres(new ArrayList<>())
            .mpa(Mpa.builder().id(resultSet.getLong("mpa_rating_id")).build())
            .build();
    }

    public static Genre mapRowToGenre(ResultSet resultSet, int rowNum) throws SQLException {
        return Genre.builder()
            .id(resultSet.getLong("genre_id"))
            .name(resultSet.getString("genre_name"))
            .build();
    }

    public static Mpa mapRowToMpa(ResultSet resultSet, int rowNum) throws SQLException {
        return Mpa.builder()
            .id(resultSet.getLong("mpa_rating_id"))
            .name(resultSet.getString("mpa_rating_name"))
            .build();
    }
}
