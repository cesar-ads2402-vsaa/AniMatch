package com.example.animematch.model;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;

@Entity
public class TemporadaCache {

    @Id
    private Long id = 1L;
    private String temporada;
    private int ano;

    public TemporadaCache() {
    }

    public TemporadaCache(String temporada, int ano) {
        this.id = 1L;
        this.temporada = temporada;
        this.ano = ano;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getTemporada() {
        return temporada;
    }

    public void setTemporada(String temporada) {
        this.temporada = temporada;
    }

    public int getAno() {
        return ano;
    }

    public void setAno(int ano) {
        this.ano = ano;
    }
}