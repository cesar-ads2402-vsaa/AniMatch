package com.example.animematch.repository;

import com.example.animematch.model.TemporadaCache;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface TemporadaCacheRepository extends JpaRepository<TemporadaCache, Long> {

    TemporadaCache findByAnoAndTemporada(int ano, String temporada);
}