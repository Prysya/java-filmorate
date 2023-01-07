package ru.yandex.practicum.filmorate.storage.mpa;

import lombok.extern.slf4j.Slf4j;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.exception.NotFoundException;
import ru.yandex.practicum.filmorate.model.Mpa;
import ru.yandex.practicum.filmorate.utils.Mapper;

import java.util.List;
import java.util.Objects;

@Slf4j
@Component
public class MpaDbStorage implements MpaStorage {
    private final String NOT_FOUND_MESSAGE = "Рейтинг ассоциации не найден";
    private final JdbcTemplate jdbcTemplate;

    public MpaDbStorage(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }


    @Override
    public Mpa get(Long id) {
        String sqlQuery = "select * from MPA_RATING where MPA_RATING_ID = ?";

        try {
            Mpa genre = jdbcTemplate.queryForObject(sqlQuery, Mapper::mapRowToMpa, id);

            if (Objects.nonNull(genre)) {
                log.info("Найден рейтинг ассоциации: {} {}", genre.getId(), genre.getName());
                return genre;
            }
        } catch (Exception ignored) {
        }

        log.info("Рейтинг ассоциации с идентификатором {} не найден.", id);
        throw new NotFoundException(NOT_FOUND_MESSAGE);
    }

    @Override
    public List<Mpa> getAll() {
        String sqlQuery = "select * from MPA_RATING";

        return jdbcTemplate.query(sqlQuery, Mapper::mapRowToMpa);
    }
}
