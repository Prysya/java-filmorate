package ru.yandex.practicum.filmorate.utils;

import lombok.experimental.UtilityClass;
import ru.yandex.practicum.filmorate.exception.BadRequestException;

import java.util.Objects;

@UtilityClass
public class NullValidator {
    public static <T> void validate(T item, String errorMessage) throws BadRequestException {
        if (Objects.isNull(item)) {
            throw new BadRequestException(errorMessage);
        }
    }
}
