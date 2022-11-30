package ru.yandex.practicum.filmorate.service;

import lombok.experimental.UtilityClass;
import ru.yandex.practicum.filmorate.model.User;

import java.util.Objects;

@UtilityClass
public class UserService {
    public void checkAndUpdateName(User user) {
        if (Objects.isNull(user.getName()) || user.getName().isBlank()) {
            user.setName(user.getLogin());
        }
    }
}
