package ru.yandex.practicum.filmorate.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.constants.UserErrorMessages;
import ru.yandex.practicum.filmorate.exception.NotFoundException;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.storage.InMemoryUserStorage;
import ru.yandex.practicum.filmorate.storage.UserStorage;

import java.util.*;

@Service
public class UserService {
    final UserStorage inMemoryUserStorage;

    @Autowired
    public UserService(InMemoryUserStorage inMemoryUserStorage) {
        this.inMemoryUserStorage = inMemoryUserStorage;
    }

    public List<User> getAll() {
        return inMemoryUserStorage.getAll();
    }

    public User create(User user) {
        return inMemoryUserStorage.add(user);
    }

    public User update(User user) throws NotFoundException {
        User newUser = inMemoryUserStorage.update(user);

        if (Objects.isNull(newUser)) {
            throw new NotFoundException(UserErrorMessages.notFound);
        }

        return newUser;
    }

    public User getById(Long id) throws NotFoundException {
        return inMemoryUserStorage.get(id);
    }

    public void addFriend(Long userId, Long friendId) throws NotFoundException {
        User user = inMemoryUserStorage.get(userId);
        User friend = inMemoryUserStorage.get(friendId);

        Set<Long> userFirends = user.getFriends();
        userFirends.add(friendId);
        user.setFriends(userFirends);

        Set<Long> friendFriends = friend.getFriends();
        friendFriends.add(userId);
        friend.setFriends(friendFriends);
    }

    public void deleteFriend(Long userId, Long friendId) throws NotFoundException {
        User user = inMemoryUserStorage.get(userId);
        User friend = inMemoryUserStorage.get(friendId);

        Set<Long> userFirends = user.getFriends();
        userFirends.remove(friendId);
        user.setFriends(userFirends);

        Set<Long> friendFriends = friend.getFriends();
        friendFriends.remove(userId);
        friend.setFriends(friendFriends);
    }

    public List<User> getMutualFriends(Long userId, Long otherId) throws NotFoundException {
        User user = inMemoryUserStorage.get(userId);
        User otherUser = inMemoryUserStorage.get(otherId);

        Set<Long> users = new HashSet<>(user.getFriends());
        users.retainAll(otherUser.getFriends());

        return parseIdsToUsers(users);
    }

    public List<User> getFriends(Long userId) throws NotFoundException {
        return parseIdsToUsers(inMemoryUserStorage.get(userId).getFriends());
    }

    /**
     * Парсит хеш сет идентификаторов в лист пользователей
     *
     * @param ids сет уникальных идентификаторов
     * @return лист {@link User}
     */
    private List<User> parseIdsToUsers(Set<Long> ids) {
        List<User> friends = new ArrayList<>();

        ids.forEach(id -> {
            try {
                friends.add(inMemoryUserStorage.get(id));
            }
            catch (NotFoundException ignored) {
            }
        });

        return friends;
    }
}
