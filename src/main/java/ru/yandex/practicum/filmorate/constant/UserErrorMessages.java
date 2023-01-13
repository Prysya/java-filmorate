package ru.yandex.practicum.filmorate.constant;

import lombok.experimental.UtilityClass;

@UtilityClass
public class UserErrorMessages {
    public static final String email = "Электронная почта не может быть пустой и должна содержать символ @";
    public static final String login = "Логин не может быть пустым и содержать пробелы";
    public static final String birthday = "Дата рождения не может быть в будущем";
    public static final String birthdayRequired = "Поле даты рождения является обязательным";
    public static final String notFound = "Пользователь c id '%d' не найден";
    public static final String notUnique = "Email или Login уже зарегистрирован";
}
