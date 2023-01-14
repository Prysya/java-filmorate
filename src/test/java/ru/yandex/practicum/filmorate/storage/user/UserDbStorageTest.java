package ru.yandex.practicum.filmorate.storage.user;

import lombok.RequiredArgsConstructor;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.DirtiesContext;
import ru.yandex.practicum.filmorate.exception.NotFoundException;
import ru.yandex.practicum.filmorate.model.User;

import java.time.LocalDate;
import java.time.Month;
import java.util.List;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest(properties = {"spring.config.name=myapp-test-h2", "spring.datasource.url=jdbc:h2:mem:trxServiceStatus"})
@DirtiesContext(classMode = DirtiesContext.ClassMode.BEFORE_EACH_TEST_METHOD)
@AutoConfigureTestDatabase
@RequiredArgsConstructor(onConstructor_ = @Autowired)
class UserDbStorageTest {
    private final UserDbStorage userStorage;

    private User.UserBuilder createBasicUser() {
        return User.builder()
            .login("login")
            .email("test@test.ru")
            .birthday(LocalDate.of(2000, Month.DECEMBER, 1));
    }

    @Test
    void shouldBeEmptyIfUserNotExists() {
        assertTrue(userStorage.get(9999999L).isEmpty());
    }

    @Test
    void shouldCreateAndFindUserInDatabase() {
        User createdUser = userStorage.add(createBasicUser().build());
        User user = userStorage.get(
            createdUser.getId()
        ).get();

        assertThat(user).hasFieldOrPropertyWithValue("id", 1L);
    }

    @Test
    void shouldReturnFilledListOfUsers() {
        final int USERS_COUNT = 5;

        for (int i = 0; i < USERS_COUNT; i += 1) {
            userStorage.add(createBasicUser().email("test@test" + i).login("login" + i).build());
        }

        List<User> users = userStorage.getAll();

        assertEquals(users.size(), USERS_COUNT);
        assertEquals(users.get(0).getEmail(), "test@test0");
    }

    @Test
    void shouldReturnEmptyListOfUsers() {
        List<User> users = userStorage.getAll();

        assertTrue(users.isEmpty());
    }

    @Test
    void shouldUpdateUser() {
        User createdUser = userStorage.add(createBasicUser().build());

        User userForUpdate = User.builder()
            .id(createdUser.getId())
            .email("new@email")
            .birthday(createdUser.getBirthday())
            .name("new name")
            .login("new login")
            .build();

        User updatedUser = userStorage.update(userForUpdate);

        assertEquals(userForUpdate, updatedUser);
    }
}