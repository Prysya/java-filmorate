package ru.yandex.practicum.filmorate.storage.user;

import lombok.extern.slf4j.Slf4j;
import org.springframework.dao.DuplicateKeyException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.constant.UserErrorMessages;
import ru.yandex.practicum.filmorate.exception.NotFoundException;
import ru.yandex.practicum.filmorate.exception.NotUniqueException;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.utils.Converter;
import ru.yandex.practicum.filmorate.utils.Mapper;

import java.sql.PreparedStatement;
import java.util.List;
import java.util.Objects;

@Slf4j
@Component
public class UserDbStorage implements UserStorage {
    private final JdbcTemplate jdbcTemplate;

    public UserDbStorage(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }


    @Override
    public User get(Long id) {
        String sqlQuery = "select * from USERS where USER_ID = ?";

        try {
            User user = jdbcTemplate.queryForObject(sqlQuery, Mapper::mapRowToUser, id);

            if (Objects.nonNull(user)) {
                log.info("Найден пользователь: {} {}", user.getId(), user.getLogin());
                return user;
            }
        } catch (Exception ignored) {
        }

        log.info("Пользователь с идентификатором {} не найден.", id);
        throw new NotFoundException(UserErrorMessages.notFound);
    }

    @Override
    public User add(User user) {
        checkAndUpdateName(user);

        String sqlQuery = "insert into USERS(email, login, birthday, name) " +
            "values (?, ?, ?, ?)";
        KeyHolder keyHolder = new GeneratedKeyHolder();

        try {
            jdbcTemplate.update(connection -> {
                PreparedStatement stmt = connection.prepareStatement(sqlQuery, new String[] {"user_id"});
                stmt.setString(1, user.getEmail());
                stmt.setString(2, user.getLogin());
                stmt.setDate(3, Converter.convertToDateViaSqlDate(user.getBirthday()));
                stmt.setString(4, user.getName());
                return stmt;
            }, keyHolder);


            Number key = keyHolder.getKey();
            if (Objects.nonNull(key)) {
                log.info("Айди созданного пользователя: {}", key);
                return get(key.longValue());
            }
        } catch (DuplicateKeyException e) {
            throw new NotUniqueException(UserErrorMessages.notUniqeEmail);
        }


        log.info("Пользователь не создан");
        return null;
    }

    @Override
    public List<User> getAll() {
        String sqlQuery = "select * from USERS";

        return jdbcTemplate.query(sqlQuery, Mapper::mapRowToUser);
    }

    @Override
    public User update(User user) {
        checkAndUpdateName(user);

        String sqlQuery = "update USERS set " +
            "EMAIL = ?, LOGIN = ?, BIRTHDAY = ?, NAME = ? " +
            "where USER_ID = ?";

        int status = jdbcTemplate.update(sqlQuery,
            user.getEmail(),
            user.getLogin(),
            user.getBirthday(),
            user.getName(),
            user.getId()
        );

        if (status > 0) {
            log.info("Пользователь с id = {} успешно обновлен", user.getId());
            return get(user.getId());
        }

        throw new NotFoundException(UserErrorMessages.notFound);
    }

    private void checkAndUpdateName(User user) {
        if (Objects.isNull(user.getName()) || user.getName().isBlank()) {
            user.setName(user.getLogin());
        }
    }
}
