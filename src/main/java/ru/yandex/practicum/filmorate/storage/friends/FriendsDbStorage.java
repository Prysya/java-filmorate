package ru.yandex.practicum.filmorate.storage.friends;

import lombok.extern.slf4j.Slf4j;
import org.springframework.dao.DuplicateKeyException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.exception.NotFoundException;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.utils.Mapper;

import java.util.List;

@Slf4j
@Component
public class FriendsDbStorage implements FriendsStorage {

    private final JdbcTemplate jdbcTemplate;

    public FriendsDbStorage(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    @Override
    public List<User> get(Long id) {
        String sqlQuery = "select U.* " +
            "from FRIENDSHIP_STATUS as FS " +
            "join USERS as U on FS.FRIEND_ID = U.USER_ID " +
            "where FS.USER_ID = ?";

        return jdbcTemplate.query(sqlQuery, Mapper::mapRowToUser, id);
    }

    @Override
    public void add(Long userId, Long friendId) {
        String sqlQuery = "insert into FRIENDSHIP_STATUS(user_id, friend_id) " +
            "values ( ?, ? )";

        try {
            jdbcTemplate.update(sqlQuery, userId, friendId);
        } catch (DuplicateKeyException ignored) {
            log.warn("Пользователь с id = {} уже отправлял запрос на дружбу пользователю с id = {}", userId, friendId);
        }
    }

    @Override
    public List<User> getMutual(Long userId, Long friendId) {
        String sqlQuery = "SELECT u.* " +
            "FROM USERS u, FRIENDSHIP_STATUS f, FRIENDSHIP_STATUS o " +
            "WHERE u.USER_ID = f.FRIEND_ID " +
            "  AND u.USER_ID = o.FRIEND_ID " +
            "  AND f.USER_ID = ? " +
            "  AND o.USER_ID = ?";

        return jdbcTemplate.query(sqlQuery, Mapper::mapRowToUser, userId, friendId);
    }

    @Override
    public void delete(Long userId, Long friendId) throws NotFoundException {
        String sqlQuery = "delete from FRIENDSHIP_STATUS " +
            "where USER_ID = ? and FRIEND_ID = ?";

        jdbcTemplate.update(sqlQuery, userId, friendId);
    }
}
