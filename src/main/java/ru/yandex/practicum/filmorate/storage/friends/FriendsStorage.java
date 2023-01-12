package ru.yandex.practicum.filmorate.storage.friends;

import ru.yandex.practicum.filmorate.model.User;

import java.util.List;

public interface FriendsStorage {
    /**
     * Получение списка друзей пользователя по уникальному идентификатору
     *
     * @param id уникальный идентификатор
     * @return список друзей
     */
    List<User> get(Long id);

    /**
     * Добавление запроса на дружбу пользователя
     *
     * @param userId   идентификатор пользователя
     * @param friendId идентификатор друга
     */
    void add(Long userId, Long friendId);

    /**
     * Получение списка общих друзей
     *
     * @param userId   идентификатор пользователя
     * @param friendId идентификатор друга
     * @return список {@link User}
     */
    List<User> getMutual(Long userId, Long friendId);

    /**
     * Удаление из друзей
     *
     * @param userId   идентификатор пользователя
     * @param friendId идентификатор друга
     */
    void delete(Long userId, Long friendId);
}
