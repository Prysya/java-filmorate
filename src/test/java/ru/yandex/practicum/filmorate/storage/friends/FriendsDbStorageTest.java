package ru.yandex.practicum.filmorate.storage.friends;

import lombok.RequiredArgsConstructor;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.DirtiesContext;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.storage.user.UserDbStorage;

import java.time.LocalDate;
import java.time.Month;
import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest(properties = {"spring.config.name=myapp-test-h2", "spring.datasource.url=jdbc:h2:mem:trxServiceStatus"})
@DirtiesContext(classMode = DirtiesContext.ClassMode.BEFORE_EACH_TEST_METHOD)
@AutoConfigureTestDatabase
@RequiredArgsConstructor(onConstructor_ = @Autowired)
class FriendsDbStorageTest {

    private final UserDbStorage userStorage;
    private final FriendsDbStorage friendsStorage;


    List<User> createBasicUsers() {
        final int USERS_COUNT = 4;
        List<User> users = new ArrayList<>();

        for (int i = 0; i < USERS_COUNT; i += 1) {
            users.add(userStorage.add(User.builder()
                .login("login" + i)
                .email("test@test.ru" + i)
                .birthday(LocalDate.of(2000, Month.DECEMBER, 1))
                .build()));
        }

        return users;
    }

    @Test
    void shouldAddAndGetUserFriends() {
        List<User> users = createBasicUsers();

        users.forEach(user -> {
            if (!user.getId().equals(1L)) {
                friendsStorage.add(users.get(0).getId(), user.getId());
            }
        });

        assertEquals(friendsStorage.get(users.get(0).getId()).size(), 3);
        assertEquals(friendsStorage.get(users.get(0).getId()).get(0).getId(), 2L);
    }

    @Test
    void shouldReturnMutualFriends() {
        List<User> users = createBasicUsers();

        User user1 = users.get(0);
        User user2 = users.get(1);
        User user3 = users.get(2);

        friendsStorage.add(user1.getId(), user2.getId());
        friendsStorage.add(user3.getId(), user2.getId());

        assertEquals(friendsStorage.getMutual(user1.getId(), user3.getId()).get(0), user2);
    }

    @Test
    void shouldDeleteFrinds() {
        List<User> users = createBasicUsers();
        User user1 = users.get(0);
        User user2 = users.get(1);

        friendsStorage.add(user1.getId(), user2.getId());
        assertEquals(friendsStorage.get(user1.getId()).size(), 1);

        friendsStorage.delete(user1.getId(), user2.getId());
        assertTrue(friendsStorage.get(user1.getId()).isEmpty());
    }
}