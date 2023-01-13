package ru.yandex.practicum.filmorate.storage.user;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.dao.DuplicateKeyException;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.constant.UserErrorMessages;
import ru.yandex.practicum.filmorate.exception.NotUniqueException;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.utils.Converter;
import ru.yandex.practicum.filmorate.utils.Mapper;

import java.sql.PreparedStatement;
import java.util.List;
import java.util.Objects;
import java.util.Optional;

@Slf4j
@Component
@RequiredArgsConstructor
public class UserDbStorage implements UserStorage {
    private final JdbcTemplate jdbcTemplate;

    @Override
    public Optional<User> get(Long id) {
        String sqlQuery = "select * " +
            "from USERS " +
            "where USER_ID = ?";

        try {
            return Optional.ofNullable(jdbcTemplate.queryForObject(sqlQuery, Mapper::mapRowToUser, id));
        } catch (EmptyResultDataAccessException e) {
            return Optional.empty();
        }
    }

    @Override
    public Optional<User> add(User user) {
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

            user.setId(Objects.requireNonNull(keyHolder.getKey()).longValue());
            return Optional.of(user);
        } catch (DuplicateKeyException e) {
            throw new NotUniqueException(UserErrorMessages.notUnique);
        } catch (NullPointerException e) {
            return Optional.empty();
        }
    }

    @Override
    public List<User> getAll() {
        String sqlQuery = "select * from USERS";

        return jdbcTemplate.query(sqlQuery, Mapper::mapRowToUser);
    }

    @Override
    public Optional<User> update(User user) {
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

        return status > 0 ? Optional.of(user) : Optional.empty();
    }
}
