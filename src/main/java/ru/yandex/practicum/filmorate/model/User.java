package ru.yandex.practicum.filmorate.model;

import lombok.Data;
import ru.yandex.practicum.filmorate.constants.UserErrorMessages;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Past;
import javax.validation.constraints.Pattern;
import java.time.LocalDate;

@Data
public class User {
    /**
     * Электронная почта
     */
    @NotBlank(message = UserErrorMessages.email)
    @Email(message = UserErrorMessages.email)
    final String email;
    /**
     * Логин пользователя
     */
    @Pattern(regexp = "^[\\S]+$", message = UserErrorMessages.login)
    final String login;
    /**
     * Дата рождения
     */
    @Past(message = UserErrorMessages.birthday)
    final LocalDate birthday;
    /**
     * Уникальный идентификатор
     */
    Integer id = null;
    /**
     * Имя для отображения
     */
    String name;
}
