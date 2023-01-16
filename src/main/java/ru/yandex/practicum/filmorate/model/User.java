package ru.yandex.practicum.filmorate.model;

import lombok.Builder;
import lombok.Data;
import ru.yandex.practicum.filmorate.constant.UserErrorMessages;
import ru.yandex.practicum.filmorate.utils.RegexPatterns;

import javax.validation.constraints.*;
import java.time.LocalDate;

@Data
@Builder
public class User {
    /**
     * Электронная почта
     */
    @NotBlank(message = UserErrorMessages.email)
    @Email(message = UserErrorMessages.email, regexp = RegexPatterns.email)
    private final String email;
    /**
     * Логин пользователя
     */
    @NotBlank(message = UserErrorMessages.login)
    @Pattern(regexp = RegexPatterns.withoutWhiteSpaces, message = UserErrorMessages.login)
    private final String login;
    /**
     * Дата рождения
     */
    @NotNull(message = UserErrorMessages.birthdayRequired)
    @PastOrPresent(message = UserErrorMessages.birthday)
    private final LocalDate birthday;
    /**
     * Уникальный идентификатор
     */
    private Long id;
    /**
     * Имя для отображения
     */
    private String name;
}
