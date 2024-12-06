package com.example.DWAI_HENRY_HUACASI.service.Impl;

import com.example.DWAI_HENRY_HUACASI.dto.FilmDetailDto;
import com.example.DWAI_HENRY_HUACASI.dto.FilmDto;
import com.example.DWAI_HENRY_HUACASI.entity.Film;
import com.example.DWAI_HENRY_HUACASI.entity.Language;
import com.example.DWAI_HENRY_HUACASI.repository.FilmRepository;
import com.example.DWAI_HENRY_HUACASI.repository.LanguageRepository;
import com.example.DWAI_HENRY_HUACASI.service.FilmService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Optional;

@Service
public class FilmServiceImpl implements FilmService {

    @Autowired
    FilmRepository filmRepository;

    @Autowired
    LanguageRepository languageRepository;

    @Override
    @Cacheable(value = "films")
    public List<FilmDto> findAllFilms() {
        List<FilmDto> films = new ArrayList<>();
        Iterable<Film> iterable = filmRepository.findAll();
        iterable.forEach(film -> {
            FilmDto filmDto = new FilmDto(film.getFilmId(),
                    film.getTitle(),
                    film.getLanguage().getName(),
                    film.getRentalDuration(),
                    film.getRentalRate());
            films.add(filmDto);
            System.out.println(filmDto);

            ///
            String g = "-".repeat(50);

            System.out.println(g);
            System.out.println("Cargando datos desde a bd....");
            System.out.println(g);
            System.out.print(String.format("%s:%s;", film.getTitle(), film.getLanguage().getName()));

            System.out.println("2: cargando datos del cachè");
            System.out.println(g);
            System.out.print(String.format("%s:%s;", film.getTitle(), film.getLanguage().getName()));
        });
        return films;
    }

    @Override
    public FilmDetailDto findFilmById(int id) {
        Optional<Film> optional = filmRepository.findById(id);
        return optional.map(film -> new FilmDetailDto(film.getFilmId(),
                film.getTitle(),
                film.getDescription(),
                film.getReleaseYear(),
                film.getLanguage().getLanguageId(),
                film.getLanguage().getName(),
                film.getRentalDuration(),
                film.getRentalRate(),
                film.getLength(),
                film.getReplacementCost(),
                film.getRating(),
                film.getSpecialFeatures(),
                film.getLastUpdate())
        ).orElse(null);
    }

    @Override
    @CacheEvict(value = "films", allEntries = true)
    public Boolean updateFilm(FilmDetailDto filmDetailDto) {
        Optional<Film> optional = filmRepository.findById(filmDetailDto.filmId());
        return optional.map(
                film -> {
                    film.setTitle(filmDetailDto.title());
                    film.setDescription(filmDetailDto.description());
                    film.setReleaseYear(filmDetailDto.releaseYear());
                    film.setRentalDuration(filmDetailDto.rentalDuration());
                    film.setRentalRate(filmDetailDto.rentalRate());
                    film.setLength(filmDetailDto.length());
                    film.setReplacementCost(filmDetailDto.replacementCost());
                    film.setRating(filmDetailDto.rating());
                    film.setSpecialFeatures(filmDetailDto.specialFeatures());
                    film.setLastUpdate(new Date());
                    filmRepository.save(film);
                    return true;
                }
        ).orElse(false);
    }

    @Override
    @CacheEvict(value = "films", allEntries = true)
    public Boolean registerFilm(FilmDetailDto filmDetailDto) {
        Film film = new Film();
        film.setTitle(filmDetailDto.title());
        film.setDescription(filmDetailDto.description());
        film.setReleaseYear(filmDetailDto.releaseYear());
        film.setRentalDuration(filmDetailDto.rentalDuration());
        film.setRentalRate(filmDetailDto.rentalRate());
        film.setLength(filmDetailDto.length());
        film.setReplacementCost(filmDetailDto.replacementCost());
        film.setRating(filmDetailDto.rating());
        film.setSpecialFeatures(filmDetailDto.specialFeatures());
        film.setLastUpdate(new Date());

        // Obtener el lenguaje correspondiente
        Language language = languageRepository.findById(filmDetailDto.languageId())
                .orElseThrow(() -> new RuntimeException("Language not found"));

        film.setLanguage(language);
        filmRepository.save(film);
        return true;

    }

    @Override
    @CacheEvict(value = "films", allEntries = true)
    public Boolean deleteFilm(int id) {
        Optional<Film> optionalFilm = filmRepository.findById(id);
        if (optionalFilm.isPresent()) {
            Film film = optionalFilm.get();
            filmRepository.delete(film);
            System.out.println("Eliminacion exitosa");
            return true;
        }
        System.out.println("No se pudo eliminar el film con id " + id);
        return false;

    }


}
