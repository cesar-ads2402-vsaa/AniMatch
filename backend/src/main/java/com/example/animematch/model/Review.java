package com.example.animematch.model;

import jakarta.persistence.*;

@Entity
@Table(name = "reviews") 
public class Review {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String author;
    private String comment;

    @ManyToOne
    @JoinColumn(name = "anime_id")
    private Anime anime;

    public Review() {
    }

    // Getters
    public Long getId() {
        return id;
    }

    public String getAuthor() {
        return author;
    }

    public String getComment() {
        return comment;
    }

    public Anime getAnime() {
        return anime;
    }

    // Setters
    public void setId(Long id) {
        this.id = id;
    }

    public void setAuthor(String author) {
        this.author = author;
    }

    public void setComment(String comment) {
        this.comment = comment;
    }

    public void setAnime(Anime anime) {
        this.anime = anime;
    }
}