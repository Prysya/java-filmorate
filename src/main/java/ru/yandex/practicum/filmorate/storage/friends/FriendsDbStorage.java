package ru.yandex.practicum.filmorate.storage.friends;

import lombok.extern.slf4j.Slf4j;
import org.springframework.dao.DuplicateKeyException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.exception.NotFoundException;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.utils.Mapper;

import java.util.List;
import java.util.Objects;

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
        String sqlQuery = "insert into FRIENDSHIP_STATUS(user_id, friend_id, is_confirmed) " +
            "values ( ?, ?, false )";

        try {
            jdbcTemplate.update(sqlQuery, userId, friendId);
        } catch (DuplicateKeyException ignored) {
            log.warn("Пользователь с id = {} уже отправлял запрос на дружбу пользователю с id = {}", userId, friendId);
        }

        if (checkFriendshipStatus(userId, friendId)) {
            log.info("Пользователь с id = {} и пользователь с id = {} теперь друзья", userId, friendId);
            setFriendsipStatus(userId, friendId, true);
        }
    }

    @Override
    public List<User> getMutual(Long userId, Long friendId) {
        String sqlQuery = "SELECT * " +
            "FROM users " +
            "WHERE user_id IN " +
            "      (SELECT friend_id " +
            "       FROM users u " +
            "                JOIN FRIENDSHIP_STATUS f ON u.user_id = f.user_id " +
            "       WHERE u.user_id = ?) " +
            "  AND user_id IN " +
            "      (SELECT friend_id " +
            "       FROM users u " +
            "                JOIN FRIENDSHIP_STATUS f ON u.user_id = f.user_id " +
            "       WHERE u.user_id = ?)";

        return jdbcTemplate.query(sqlQuery, Mapper::mapRowToUser, userId, friendId);
    }

    @Override
    public void delete(Long userId, Long friendId) throws NotFoundException {
        String sqlQuery = "delete from FRIENDSHIP_STATUS " +
            "where USER_ID = ? and FRIEND_ID = ?";

        jdbcTemplate.update(sqlQuery, userId, friendId);

        if (!checkFriendshipStatus(userId, friendId)) {
            log.info("Пользователь с id = {} и пользователь с id = {} больше не друзья", userId, friendId);
            setFriendsipStatus(userId, friendId, false);
        }
    }

    private boolean checkFriendshipStatus(Long userId, Long friendId) {
        final int COUNT_IF_FRIENDS = 2;

        String sqlQuery = "select COUNT(*) as friendship_status " +
            "from FRIENDSHIP_STATUS " +
            "where FRIEND_ID in (?, ?) and USER_ID in (?, ?)";

        Integer count = jdbcTemplate.queryForObject(sqlQuery, Integer.class, userId, friendId, userId, friendId);

        return Objects.nonNull(count) && count > COUNT_IF_FRIENDS;
    }

    private void setFriendsipStatus(Long userId, Long friendId, Boolean status) {
        String sqlQuery = "update FRIENDSHIP_STATUS " +
            "set IS_CONFIRMED = ? " +
            "where USER_ID in (?, ?) and FRIEND_ID in (?, ?)";

        jdbcTemplate.update(sqlQuery,
            status,
            userId,
            friendId,
            userId,
            friendId
        );
    }
}
