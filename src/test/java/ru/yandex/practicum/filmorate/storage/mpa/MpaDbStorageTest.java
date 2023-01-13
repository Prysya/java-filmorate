package ru.yandex.practicum.filmorate.storage.mpa;

import lombok.RequiredArgsConstructor;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.DirtiesContext;
import ru.yandex.practicum.filmorate.model.Mpa;
import ru.yandex.practicum.filmorate.storage.likes.LikesStorage;
import ru.yandex.practicum.filmorate.storage.user.UserDbStorage;

import java.util.List;
import java.util.Objects;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest(properties = {"spring.config.name=myapp-test-h2", "spring.datasource.url=jdbc:h2:mem:trxServiceStatus"})
@DirtiesContext(classMode = DirtiesContext.ClassMode.BEFORE_EACH_TEST_METHOD)
@AutoConfigureTestDatabase
@RequiredArgsConstructor(onConstructor_ = @Autowired)
class MpaDbStorageTest {
    private final MpaDbStorage mpaStrorage;

    @Test
    void shouldReturnMpaById() {
        Mpa mpa = mpaStrorage.get(1L).get();

        assertEquals(mpa.getClass(), Mpa.class);
        assertTrue(Objects.nonNull(mpa.getId()));
        assertThat(mpa).hasFieldOrPropertyWithValue("id", 1L);
    }

    @Test
    void shouldReturnListOfMpa() {
        List<Mpa> mpaList =  mpaStrorage.getAll();

        assertFalse(mpaList.isEmpty());
        assertEquals(mpaList.get(0).getClass(), Mpa.class);
        assertTrue(Objects.nonNull(mpaList.get(0).getId()));
        assertThat(mpaList.get(0)).hasFieldOrPropertyWithValue("id", 1L);
    }
}