package ru.yandex.practicum.filmorate.storage.user;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.constant.UserErrorMessages;
import ru.yandex.practicum.filmorate.exception.NotFoundException;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.utils.NullValidator;

import java.util.*;

@Slf4j
@Component
public class InMemoryUserStorage implements UserStorage {

    /**
     * Мапа пользователей по уникальному идентификатору
     */
    private final Map<Long, User> users = new HashMap<>();

    @Override
    public User get(Long id) throws NotFoundException {
        if (users.containsKey(id)) {
            return users.get(id);

        }

        throw new NotFoundException(UserErrorMessages.notFound);
    }

    @Override
    public User add(User user) {
        NullValidator.validate(user, UserErrorMessages.badRequest);
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
        NullValidator.validate(user, UserErrorMessages.badRequest);
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

    private void checkAndUpdateName(User user) {
        if (Objects.isNull(user.getName()) || user.getName().isBlank()) {
            user.setName(user.getLogin());
        }
    }
}
