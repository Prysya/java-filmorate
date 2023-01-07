package ru.yandex.practicum.filmorate.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.exception.NotFoundException;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.storage.friends.FriendsStorage;
import ru.yandex.practicum.filmorate.storage.user.UserStorage;

import java.util.List;

@Service
public class UserService {
    private final UserStorage userStorage;
    private final FriendsStorage friendsStorage;

    @Autowired
    public UserService(
        @Qualifier("userDbStorage") UserStorage userStorage,
        @Qualifier("friendsDbStorage") FriendsStorage friendsStorage
    ) {
        this.userStorage = userStorage;
        this.friendsStorage = friendsStorage;
    }

    public List<User> getAll() {
        return userStorage.getAll();
    }

    public User create(User user) {
        return userStorage.add(user);

    }

    public User update(User user) throws NotFoundException {
        return userStorage.update(user);
    }

    public User getById(Long id) throws NotFoundException {
        return userStorage.get(id);
    }

    public void addFriend(Long userId, Long friendId) throws NotFoundException {
        userStorage.get(userId);
        userStorage.get(friendId);

        friendsStorage.add(userId, friendId);
    }

    public void deleteFriend(Long userId, Long friendId) throws NotFoundException {
        userStorage.get(userId);
        userStorage.get(friendId);

        friendsStorage.delete(userId, friendId);
    }

    public List<User> getMutualFriends(Long userId, Long otherId) throws NotFoundException {
        userStorage.get(userId);
        userStorage.get(otherId);

        return friendsStorage.getMutual(userId, otherId);
    }

    public List<User> getFriends(Long userId) throws NotFoundException {
        return friendsStorage.get(userId);
    }
}
