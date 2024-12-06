package com.example.DWAI_HENRY_HUACASI.service;

import com.example.DWAI_HENRY_HUACASI.dto.FilmDetailDto;
import com.example.DWAI_HENRY_HUACASI.dto.FilmDto;

import java.util.List;

public interface FilmService {
    List<FilmDto> findAllFilms();

    FilmDetailDto findFilmById(int id);

    Boolean updateFilm(FilmDetailDto filmDetailDto);

    Boolean registerFilm(FilmDetailDto filmDetailDto);

    Boolean deleteFilm(int id);
}
