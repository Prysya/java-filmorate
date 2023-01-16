package ru.yandex.practicum.filmorate.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import ru.yandex.practicum.filmorate.model.FilmGenre;
import ru.yandex.practicum.filmorate.service.FilmGenreService;

import java.util.List;

@Slf4j
@RestController
@RequestMapping("/genres")
@RequiredArgsConstructor
public class GenreController {
    private final FilmGenreService filmGenreService;


    @GetMapping
    public List<FilmGenre> getAll() {
        return filmGenreService.getAll();
    }

    @GetMapping("/{genreId}")
    public FilmGenre getById(@PathVariable Long genreId) {
        return filmGenreService.getById(genreId);
    }
}
