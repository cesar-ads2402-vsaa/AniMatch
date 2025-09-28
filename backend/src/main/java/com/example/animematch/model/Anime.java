package com.example.animematch.model;
import jakarta.persistence.Embedded;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.ElementCollection;
import jakarta.persistence.CollectionTable;
import jakarta.persistence.Column;    
import jakarta.persistence.JoinColumn;
import jakarta.persistence.Lob;

import java.util.List;

import lombok.Getter;
import lombok.Setter;

@Entity
@Getter
@Setter
public class Anime {
    @Id
    private Long id;
    private String tituloPrincipal;
    private String status;
    private String classificacao;
    private Double nota;
    private Integer episodios;
    @Lob
    private String sinopse;
    private String urlTrailer;
    private int ano;
    private int popularidade;
    
    @ElementCollection
    @CollectionTable(name = "anime_generos", joinColumns = @JoinColumn(name = "anime_id"))
    @Column(name = "genero")
    private List<String> generos;

    @ElementCollection
    @CollectionTable(name = "anime_estudios", joinColumns = @JoinColumn(name = "anime_id"))
    @Column(name = "estudio")
    private List<String> estudios;
    
    @ElementCollection
    @CollectionTable(name = "anime_reviews", joinColumns = @JoinColumn(name = "anime_id"))
    @Column(name = "review_text")
    @Lob
    private List<String> reviews;

    @Embedded
    private Periodo periodoExibicao;

    @Embedded
    private Imagens imagens;

    public Anime() {
    }

    public Anime(Long id, String tituloPrincipal, String status, String classificacao, Double nota, Integer episodios,
                 String sinopse, String urlTrailer, int ano, int popularidade, List<String> generos,
                 List<String> estudios, List<String> reviews, Periodo periodoExibicao, Imagens imagens) {
        this.id = id;
        this.tituloPrincipal = tituloPrincipal;
        this.status = status;
        this.classificacao = classificacao;
        this.nota = nota;
        this.episodios = episodios;
        this.sinopse = sinopse;
        this.urlTrailer = urlTrailer;
        this.ano = ano;
        this.popularidade = popularidade;
        this.generos = generos;
        this.estudios = estudios;
        this.reviews = reviews;
        this.periodoExibicao = periodoExibicao;
        this.imagens = imagens;
    }
}
