package ru.yandex.practicum.filmorate.storage;

import ru.yandex.practicum.filmorate.exception.NotFoundException;
import ru.yandex.practicum.filmorate.model.Film;

import java.util.List;

public interface Storage<T> {
    /**
     * Получение элемента по уникальному идентификатору
     *
     * @param id уникальный идентификатор
     * @return найденный элемент
     * @throws NotFoundException если элемент не найден
     */
    T get(Long id) throws NotFoundException;

    /**
     * Добавление элемента в список
     *
     * @param item новый элемент
     * @return созданный элемент
     */
    T add(T item);

    /**
     * Получение листа всех элементов
     *
     * @return список {@link Film}
     */
    List<T> getAll();

    /**
     * Обновление элемента
     *
     * @param item элемент
     * @return обновленный элемент
     * @throws NotFoundException если элемент не найден
     */
    T update(T item) throws NotFoundException;
}
