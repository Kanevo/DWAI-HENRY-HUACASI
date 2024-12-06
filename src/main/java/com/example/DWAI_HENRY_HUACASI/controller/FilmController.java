package com.example.DWAI_HENRY_HUACASI.controller;

import com.example.DWAI_HENRY_HUACASI.dto.FilmDetailDto;
import com.example.DWAI_HENRY_HUACASI.dto.FilmDto;
import com.example.DWAI_HENRY_HUACASI.entity.Language;
import com.example.DWAI_HENRY_HUACASI.repository.LanguageRepository;
import com.example.DWAI_HENRY_HUACASI.service.FilmService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import com.example.DWAI_HENRY_HUACASI.entity.Film;
import java.util.ArrayList;

import java.util.List;

@Controller
@RequestMapping("/film")
public class FilmController {

    @Autowired
    FilmService filmService;

    @Autowired
    LanguageRepository languageRepository;

    @GetMapping("/start")
    public String start(Model model) {

        List<FilmDto> films = filmService.findAllFilms();
        System.out.println(films);
        model.addAttribute("films", films);
        return "film";

    }

    @GetMapping("/detail/{id}")
    public String detail(@PathVariable Integer id, Model model) {
        FilmDetailDto filmDetailDto = filmService.findFilmById(id);
        model.addAttribute("film", filmDetailDto);
        return "film_detail";
    }
    @GetMapping("/edit/{id}")
    public String edit(@PathVariable Integer id, Model model) {
        FilmDetailDto filmDetailDto = filmService.findFilmById(id);
        model.addAttribute("film", filmDetailDto);
        return "film_edit";
    }

    @PostMapping("/edit-confirm")
    public String editConfirm(@ModelAttribute FilmDetailDto filmDetailDto, Model model) {
        filmService.updateFilm(filmDetailDto);
        return "redirect:/film/start";
    }

    @GetMapping("/new-film")
    public String newFilm(Model model) {
        List<Language> languages = languageRepository.findAll();
        model.addAttribute("languages", languages);
        model.addAttribute("film", new FilmDetailDto(null, null, null, null, null, null, null, null, null, null, null, null, null));
        return "film_create";
    }

    @PostMapping("/register-film")
    public String registerFilm(@ModelAttribute FilmDetailDto filmDetailDto) {
        filmService.registerFilm(filmDetailDto);
        return "redirect:/film/start";
    }


    @GetMapping("/film/remove/{id}")
    public String removeFilm(@PathVariable int id) {
        boolean deleted = filmService.deleteFilm(id);
        if (deleted) {
            return "redirect:/film"; // Redirige a la lista de películas después de eliminar
        } else {
            // Manejar el caso en que no se pudo eliminar
            return "error";
        }
    }
}
