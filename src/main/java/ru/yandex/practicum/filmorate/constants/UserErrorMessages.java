package ru.yandex.practicum.filmorate.constants;

import lombok.experimental.UtilityClass;

@UtilityClass
public class UserErrorMessages {
    public static final String email = "Электронная почта не может быть пустой и должна содержать символ @";
    public static final String login = "Логин не может быть пустым и содержать пробелы";
    public static final String birthday = "Дата рождения не может быть в будущем";
    public static final String notFound = "Пользователь не найден";
    public static final String badRequest = "Ошибка валидации полей пользователя";
}
