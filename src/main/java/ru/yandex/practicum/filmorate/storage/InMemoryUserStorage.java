package ru.yandex.practicum.filmorate.storage;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.constants.UserErrorMessages;
import ru.yandex.practicum.filmorate.exception.NotFoundException;
import ru.yandex.practicum.filmorate.model.User;

import java.util.*;

@Slf4j
@Component
public class InMemoryUserStorage implements UserStorage {

    /**
     * Мапа пользователей по уникальному идентификатору
     */
    final Map<Long, User> users = new HashMap<>();

    @Override
    public User get(Long id) throws NotFoundException {
        if (users.containsKey(id)) {
            return users.get(id);

        }

        throw new NotFoundException(UserErrorMessages.notFound);
    }

    @Override
    public User add(User user) {
        checkAndUpdateName(user);

        user.setId((long) (users.size() + 1));
        users.put(user.getId(), user);

        log.debug("Создан новый пользователь: {}", user);

        return user;
    }

    @Override
    public List<User> getAll() {
        return new ArrayList<>(users.values());
    }

    @Override
    public User update(User user) throws NotFoundException {
        boolean hasId = Objects.nonNull(user.getId());

        if (hasId) {
            if (users.containsKey(user.getId())) {
                checkAndUpdateName(user);
                users.put(user.getId(), user);

                log.debug("Обновлен пользователь с id({}): {}", user.getId(), user);

                return user;
            }

            log.debug("Пользователь с id({}) не найден", user.getId());
            throw new NotFoundException(UserErrorMessages.notFound);
        }


        return add(user);
    }

    public void checkAndUpdateName(User user) {
        if (Objects.isNull(user.getName()) || user.getName().isBlank()) {
            user.setName(user.getLogin());
        }
    }
}
