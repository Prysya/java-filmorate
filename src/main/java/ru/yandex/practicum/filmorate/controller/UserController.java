package ru.yandex.practicum.filmorate.controller;

import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;
import ru.yandex.practicum.filmorate.constants.UserErrorMessages;
import ru.yandex.practicum.filmorate.exception.NotFoundException;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.service.UserService;

import javax.validation.Valid;
import java.util.*;

@Slf4j
@RestController
@RequestMapping("/users")
public class UserController {
    final Map<Integer, User> users = new HashMap<>();

    @GetMapping
    public List<User> getAll() {
        return new ArrayList<>(users.values());
    }

    @PostMapping
    public User create(@Valid @RequestBody User user) {
        UserService.checkAndUpdateName(user);

        user.setId(users.size() + 1);
        users.put(user.getId(), user);

        log.debug("Создан новый пользователь: {}", user);

        return user;
    }

    @PutMapping
    public User update(@Valid @RequestBody User user) throws NotFoundException {
        boolean hasId = Objects.nonNull(user.getId());

        if (hasId) {
            if (users.containsKey(user.getId())) {
                UserService.checkAndUpdateName(user);
                users.put(user.getId(), user);

                log.debug("Обновлен пользователь с id({}): {}", user.getId(), user);

                return user;
            }

            throw new NotFoundException(UserErrorMessages.notFound);
        }


        return create(user);
    }
}
