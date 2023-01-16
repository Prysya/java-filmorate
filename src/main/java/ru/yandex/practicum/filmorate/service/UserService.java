package ru.yandex.practicum.filmorate.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.constant.UserErrorMessages;
import ru.yandex.practicum.filmorate.exception.NotFoundException;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.storage.friends.FriendsStorage;
import ru.yandex.practicum.filmorate.storage.user.UserStorage;

import java.util.List;
import java.util.Objects;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class UserService {
    private final UserStorage userStorage;
    private final FriendsStorage friendsStorage;

    public List<User> getAll() {
        return userStorage.getAll();
    }

    public User create(User user) {
        return userStorage.add(checkAndUpdateName(user));
    }

    public User update(User user) {
        return userStorage.update(checkAndUpdateName(user));
    }

    public User getById(Long id) {
        return checkUserExists(userStorage.get(id), id);

    }

    public void addFriend(Long userId, Long friendId) {
        checkUserExists(userStorage.get(userId), userId);
        checkUserExists(userStorage.get(friendId), friendId);

        friendsStorage.add(userId, friendId);
    }

    public void deleteFriend(Long userId, Long friendId) {
        checkUserExists(userStorage.get(userId), userId);
        checkUserExists(userStorage.get(friendId), friendId);

        friendsStorage.delete(userId, friendId);
    }

    public List<User> getMutualFriends(Long userId, Long otherId) {
        checkUserExists(userStorage.get(userId), userId);
        checkUserExists(userStorage.get(otherId), otherId);

        return friendsStorage.getMutual(userId, otherId);
    }

    public List<User> getFriends(Long userId) {
        return friendsStorage.get(userId);
    }

    /**
     * Проверка на наличие данных в поле name и в случае отсутствия заполняет данные
     * из поля логина
     *
     * @param user {@link User}
     * @return {@link User}
     */
    private User checkAndUpdateName(User user) {
        if (Objects.isNull(user.getName()) || user.getName().isBlank()) {
            user.setName(user.getLogin());
        }

        return user;
    }

    /**
     * Проверка наличия данных в Optional или выброс 404 ошибки
     *
     * @param user Опциональный {@link User}
     * @return {@link User}
     * @throws NotFoundException если пользователь не найден
     */
    private User checkUserExists(Optional<User> user, Long userId) {
        return user
            .orElseThrow(() -> new NotFoundException(String.format(UserErrorMessages.notFound, userId)));
    }
}
