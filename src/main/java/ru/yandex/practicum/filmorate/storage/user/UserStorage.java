package ru.yandex.practicum.filmorate.storage.user;

import ru.yandex.practicum.filmorate.model.User;

import java.util.List;
import java.util.Optional;

public interface UserStorage {
    /**
     * Получение пользователя по уникальному идентификатору
     *
     * @param id уникальный идентификатор
     * @return найденный пользователь
     */
    Optional<User> get(Long id);

    /**
     * Добавление пользователя в базу
     *
     * @param user новый пользователь
     * @return созданный пользователь
     */
    Optional<User> add(User user);

    /**
     * Получение листа всех пользователей
     *
     * @return список {@link User}
     */
    List<User> getAll();

    /**
     * Обновление пользователя
     *
     * @param user пользователь
     * @return обновленный пользователь
     */
    Optional<User> update(User user);
}
