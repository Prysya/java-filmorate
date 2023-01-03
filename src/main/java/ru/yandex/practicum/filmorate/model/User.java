package ru.yandex.practicum.filmorate.model;

import lombok.Data;
import ru.yandex.practicum.filmorate.constant.UserErrorMessages;
import ru.yandex.practicum.filmorate.utils.RegexPatterns;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.PastOrPresent;
import javax.validation.constraints.Pattern;
import java.time.LocalDate;
import java.util.HashSet;
import java.util.Set;

@Data
public class User {
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
    /**
     * Уникальный идентификатор
     */
    private Long id = null;
    /**
     * Имя для отображения
     */
    private String name;
    /**
     * Список уникальных идентификаторов друзей
     */
    private Set<Long> friends = new HashSet<>();
}
