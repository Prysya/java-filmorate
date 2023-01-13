package ru.yandex.practicum.filmorate.constant;

import lombok.experimental.UtilityClass;

@UtilityClass
public class FilmErrorMessages {
    public static final String name = "Название не может быть пустым";
    public static final String description = "Превышена максимальная длина описания";
    public static final String releaseDate = "Дата релиза — не раньше 28 декабря 1895 года";
    public static final String duration = "Продолжительность фильма должна быть положительной";
    public static final String notFound = "Фильм c id: '%d' не найден";
    public static final String mpaIsEmpty = "Рейтинг ассоциации конокомпаний не может быть пустым";
}
