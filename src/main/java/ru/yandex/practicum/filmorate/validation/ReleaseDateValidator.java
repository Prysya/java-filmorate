package ru.yandex.practicum.filmorate.validation;

import ru.yandex.practicum.filmorate.annotation.validator.ReleaseDateValidation;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;
import java.time.LocalDate;
import java.util.Calendar;

public class ReleaseDateValidator implements ConstraintValidator<ReleaseDateValidation, LocalDate> {
    @Override
    public boolean isValid(LocalDate date, ConstraintValidatorContext cxt) {
        LocalDate minDate = LocalDate.of(1895, Calendar.DECEMBER, 28);
        return date.isAfter(minDate);
    }
}
