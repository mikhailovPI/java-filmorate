package ru.yandex.practicum.filmorate.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.exception.NotFoundException;
import ru.yandex.practicum.filmorate.exception.ValidationException;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.storage.film.FilmStorage;
import ru.yandex.practicum.filmorate.storage.user.UserStorage;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class FilmService {

    private final FilmStorage filmStorage;
    private final UserStorage userStorage;

    @Autowired
    public FilmService(FilmStorage filmStorage, UserStorage userStorage) {
        this.filmStorage = filmStorage;
        this.userStorage = userStorage;
    }

    public List<Film> getFilm() {
        return filmStorage.getFilms();
    }

    public Film getFilmById(Long filmId) {
        return filmStorage.getFilmById(filmId);
    }

    public Film createFilm(Film film) throws ValidationException {
        if (film == null) {
            throw new NotFoundException("Передан пустой фильм.");
        }
        return filmStorage.createFilm(film);
    }

    public Film updateFilm(Film film) throws ValidationException {
        if (filmStorage.getFilmById(film.getId()) == null) {
            throw new NotFoundException("Фильм не найден для обновления.");
        }
        return filmStorage.updateFilm(film);
    }

    public void removeFilm(Film film) throws ValidationException {
        if (filmStorage.getFilmById(film.getId()) == null) {
            throw new NotFoundException("Фильм не найден для удаления.");
        }
        filmStorage.deleteFilm(film);
    }

    public Integer putLike(Long idFilm, Long id) {
        if (filmStorage.getFilmById(idFilm).getLike().contains(id)) {
            throw new ValidationException("Пользователь " + userStorage.getUserById(id) + " уже оценивал этот фильм.");
        }
        filmStorage.getFilmById(idFilm).getLike().add(id);
        return filmStorage.getFilmById(idFilm).getLike().size();
    }

    public Integer removeLike(Long idFilm, Long id) {
        if (!filmStorage.getFilmById(idFilm).getLike().contains(id)) {
            throw new ValidationException("Пользователь " + userStorage.getUserById(id) + " не оценивал этот фильм.");
        }
        filmStorage.getFilmById(idFilm).getLike().remove(id);
        return filmStorage.getFilmById(idFilm).getLike().size();
    }

    public List<Film> getTopLikeFilm(Integer count) {
        return filmStorage.getFilms().stream().sorted((p0, p1) ->
                        p1.getLike().size() - (p0.getLike().size())).
                limit(count).collect(Collectors.toList());
    }
}
