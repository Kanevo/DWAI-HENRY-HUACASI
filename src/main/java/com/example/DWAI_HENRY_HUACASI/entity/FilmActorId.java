package com.example.DWAI_HENRY_HUACASI.entity;

import jakarta.persistence.Embeddable;

@Embeddable
public class FilmActorId {
    private Integer filmId;
    private Integer actorId;
}
