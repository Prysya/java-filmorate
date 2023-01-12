package ru.yandex.practicum.filmorate.storage.user;

import ru.yandex.practicum.filmorate.model.User;

import java.util.List;

public interface UserStorage {
    /**
     * Получение пользователя по уникальному идентификатору
     *
     * @param id уникальный идентификатор
     * @return найденный пользователь
     */
    User get(Long id);

    /**
     * Добавление пользователя в список
     *
     * @param user новый пользователь
     * @return созданный пользователь
     */
    User add(User user);

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
    User update(User user);
}
