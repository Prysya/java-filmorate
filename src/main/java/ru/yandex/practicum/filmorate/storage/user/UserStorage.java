package ru.yandex.practicum.filmorate.storage.user;

import ru.yandex.practicum.filmorate.model.Film;
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
     * @param item новый пользователь
     * @return созданный пользователь
     */
    User add(User item);

    /**
     * Получение листа всех пользовательов
     *
     * @return список {@link Film}
     */
    List<User> getAll();

    /**
     * Обновление пользователя
     *
     * @param item пользователь
     * @return обновленный пользователь
     */
    User update(User item);
}
