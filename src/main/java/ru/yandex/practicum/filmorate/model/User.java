package ru.yandex.practicum.filmorate.model;

import lombok.Data;
import ru.yandex.practicum.filmorate.constants.UserErrorMessages;
import ru.yandex.practicum.filmorate.utils.RegexPatterns;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.PastOrPresent;
import javax.validation.constraints.Pattern;
import java.time.LocalDate;

@Data
public class User {
    /**
     * Уникальный идентификатор
     */
    private Integer id = null;
    /**
     * Имя для отображения
     */
    private String name;
    /**
     * Электронная почта
     */
    @NotBlank(message = UserErrorMessages.email)
    @Email(message = UserErrorMessages.email, regexp = RegexPatterns.EMAIL)
    private final String email;
    /**
     * Логин пользователя
     */
    @NotBlank(message = UserErrorMessages.login)
    @Pattern(regexp = RegexPatterns.WITHOUT_WHITE_SPACES, message = UserErrorMessages.login)
    private final String login;
    /**
     * Дата рождения
     */
    @PastOrPresent(message = UserErrorMessages.birthday)
    private final LocalDate birthday;
}
