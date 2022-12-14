package ru.yandex.practicum.filmorate.controller;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import ru.yandex.practicum.filmorate.exception.NotFoundException;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.service.FilmService;

import javax.validation.Valid;
import java.util.List;
import java.util.Optional;

@Slf4j
@RestController
@RequestMapping("/films")
public class FilmController {

    private final FilmService filmService;

    @Autowired
    public FilmController(FilmService filmService) {
        this.filmService = filmService;
    }

    @GetMapping
    public List<Film> getAll() {
        return filmService.getAll();
    }

    @PostMapping
    public Film create(@Valid @RequestBody Film film) {
        return filmService.create(film);
    }

    @PutMapping
    public Film update(@Valid @RequestBody Film film) throws NotFoundException {
        return filmService.update(film);
    }

    @GetMapping("/{filmId}")
    public Film getById(@PathVariable Long filmId) throws NotFoundException {
        return filmService.getById(filmId);
    }

    @PutMapping("/{filmId}/like/{userId}")
    public void likeFilm(@PathVariable Long filmId, @PathVariable Long userId) throws NotFoundException {
        filmService.addLike(filmId, userId);
    }

    @DeleteMapping("/{filmId}/like/{userId}")
    public void dislikeFilm(@PathVariable Long filmId, @PathVariable Long userId) throws NotFoundException {
        filmService.deleteLike(filmId, userId);
    }

    @GetMapping("/popular")
    public List<Film> getById(@RequestParam(required = false) Optional<Integer> count) {
        return filmService.getMostLiked(count);
    }
}
