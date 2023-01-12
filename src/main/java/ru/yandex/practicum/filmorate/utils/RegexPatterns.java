package ru.yandex.practicum.filmorate.utils;

import lombok.experimental.UtilityClass;

@UtilityClass
public class RegexPatterns {
    public final String email = "^(?=.{1,64}@)[A-Za-z0-9_-]+(\\.[A-Za-z0-9_-]+)*@"
        + "[^-][A-Za-z0-9-]+(\\.[A-Za-z0-9-]+)*(\\.[A-Za-z]{2,})$";
    public final String withoutWhiteSpaces = "^[\\S]+$";
}
