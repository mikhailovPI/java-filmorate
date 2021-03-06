
package ru.yandex.practicum.filmorate.storage.film;

import ru.yandex.practicum.filmorate.model.Film;

import java.util.List;

public interface FilmStorage {

    public Film createFilm(Film film);

    public Film updateFilm(Film film);

    public void deleteFilm(Film film);

    public List<Film> getFilms();

    Film getFilmById(Long filmId);
}

