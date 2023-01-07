package ru.yandex.practicum.filmorate.storage.likes;

import lombok.RequiredArgsConstructor;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.DirtiesContext;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.model.Mpa;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.storage.film.FilmDbStorage;
import ru.yandex.practicum.filmorate.storage.user.UserDbStorage;
import static org.junit.jupiter.api.Assertions.*;

import java.time.LocalDate;
import java.time.Month;
import java.util.ArrayList;
import java.util.List;

@SpringBootTest(properties = {"spring.config.name=myapp-test-h2", "spring.datasource.url=jdbc:h2:mem:trxServiceStatus"})
@DirtiesContext(classMode = DirtiesContext.ClassMode.BEFORE_EACH_TEST_METHOD)
@AutoConfigureTestDatabase
@RequiredArgsConstructor(onConstructor_ = @Autowired)
class LikesDbStorageTest {
    private final UserDbStorage userStorage;
    private final LikesDbStorage likesStorage;
    private final FilmDbStorage filmStorage;

    List<User> createBasicUsers() {
        final int USERS_COUNT = 3;
        List<User> users = new ArrayList<>();

        for (int i = 0; i < USERS_COUNT; i += 1) {
            users.add(userStorage.add(User.builder()
                .login("login")
                .email("test@test.ru" + i)
                .birthday(LocalDate.of(2000, Month.DECEMBER, 1))
                .build()));
        }

        return users;
    }

    private Film createBasicFilm() {
        return filmStorage.add(Film.builder()
            .name("Film")
            .duration(2)
            .releaseDate(LocalDate.now())
            .mpa(Mpa.builder().id(1L).build())
            .description("")
            .build());
    }

    @Test
    void shouldAddAndGetLikes() {
        Film film = createBasicFilm();
        List<User> users = createBasicUsers();

        List<Long> emptyLikes = likesStorage.get(film.getId());
        assertTrue(emptyLikes.isEmpty());

        users.forEach(user -> likesStorage.add(film.getId(), user.getId()));
        List<Long> likes = likesStorage.get(film.getId());

        assertEquals(likes.size(), users.size());
        users.forEach(user -> assertTrue(likes.contains(user.getId())));
    }

    @Test
    void shouldDeleteLikes() {
        Film film = createBasicFilm();
        List<User> users = createBasicUsers();

        likesStorage.add(film.getId(), users.get(0).getId());
        List<Long> likes = likesStorage.get(film.getId());
        assertEquals(likes.size(), 1);

        likesStorage.delete(film.getId(), users.get(0).getId());
        List<Long> emptyLikes = likesStorage.get(film.getId());
        assertTrue(emptyLikes.isEmpty());
    }

    @Test
    void shouldReturnFilmsOrederedByLikesCount() {
        Film film1 = createBasicFilm();
        Film film2 = createBasicFilm();
        Film film3 = createBasicFilm();
        List<User> users = createBasicUsers();

        users.forEach(user -> likesStorage.add(film3.getId(), user.getId()));
        likesStorage.add(film2.getId(), users.get(0).getId());

        List<Film> films = likesStorage.getMostLikedFilms(3);

        assertEquals(films, List.of(film3, film2, film1));
    }
}