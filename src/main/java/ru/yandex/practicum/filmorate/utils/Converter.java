package ru.yandex.practicum.filmorate.utils;

import lombok.experimental.UtilityClass;

import java.sql.Date;
import java.time.LocalDate;

@UtilityClass
public class Converter {
    public Date convertToDateViaSqlDate(LocalDate dateToConvert) {
        return Date.valueOf(dateToConvert);
    }
}
