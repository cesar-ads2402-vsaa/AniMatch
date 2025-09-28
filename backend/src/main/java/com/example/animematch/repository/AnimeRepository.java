package com.example.animematch.repository;

import com.example.animematch.model.Anime;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.Optional;
import java.util.List;

public interface AnimeRepository extends JpaRepository<Anime, Long> {
    Optional<Anime> findByTituloPrincipal(String titulo);

    List<Anime> findByGenerosContaining(String genero);

    List<Anime> findByClassificacao(String classificacao);

    List<Anime> findByStatus(String status);
}