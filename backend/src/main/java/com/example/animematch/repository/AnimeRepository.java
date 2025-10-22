package com.example.animematch.repository;

import com.example.animematch.model.Anime;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import java.util.Optional;
import java.util.List;

public interface AnimeRepository extends JpaRepository<Anime, Long> {
    Optional<Anime> findByTituloPrincipal(String titulo);

    List<Anime> findByGenerosContaining(String genero);

    List<Anime> findByClassificacao(String classificacao);

    List<Anime> findByStatus(String status);

    @Query("SELECT a FROM Anime a WHERE " +
           "(:genero IS NULL OR :genero MEMBER OF a.generos) AND " +
           "(:classificacao IS NULL OR a.classificacao = :classificacao) AND " +
           "(:status IS NULL OR a.status = :status) AND " +
           "(:palavraChave IS NULL OR LOWER(a.tituloPrincipal) LIKE LOWER(CONCAT('%', :palavraChave, '%')))")
    List<Anime> findByFiltros(@Param("genero") String genero, 
                             @Param("classificacao") String classificacao, 
                             @Param("status") String status,
                             @Param("palavraChave") String palavraChave);
}