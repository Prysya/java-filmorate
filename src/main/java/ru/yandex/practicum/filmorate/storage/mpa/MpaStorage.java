package ru.yandex.practicum.filmorate.storage.mpa;

import ru.yandex.practicum.filmorate.model.Mpa;

import java.util.List;

public interface MpaStorage {
    /**
     * Получение рейтинга по уникальному идентификатору
     *
     * @param id уникальный идентификатор
     * @return найденный рейтинг ассоциации
     */
    Mpa get(Long id);

    /**
     * Получение всего списка рейтингов
     *
     * @return список рейтинга ассоциации
     */
    List<Mpa> getAll();
}
