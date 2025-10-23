package com.example.animematch.service;

import com.example.animematch.client.JikanClient;
import com.example.animematch.model.Anime;
import com.example.animematch.model.TemporadaCache;
import com.example.animematch.repository.AnimeRepository;
import com.example.animematch.repository.TemporadaCacheRepository;
import com.example.animematch.util.TemporadaUtil;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.time.LocalDate;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyList;
import static org.mockito.Mockito.*;

class AnimeServiceTest {

    @Mock
    private AnimeRepository animeRepository;

    @Mock
    private TemporadaCacheRepository temporadaCacheRepository;

    @Mock
    private JikanClient jikanClient;

    @InjectMocks
    private AnimeService animeService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void deveRetornarAnimeQuandoEncontradoPorId() {
        Anime anime = new Anime();
        anime.setId(1L);
        anime.setTituloPrincipal("Naruto");

        when(animeRepository.findById(1L)).thenReturn(Optional.of(anime));

        Anime resultado = animeService.buscarPorId(1L);

        assertNotNull(resultado);
        assertEquals("Naruto", resultado.getTituloPrincipal());
        verify(animeRepository, times(1)).findById(1L);
    }

    @Test
    void deveRetornarAnimeQuandoEncontradoPorTitulo() {
        Anime anime = new Anime();
        anime.setId(1L);
        anime.setTituloPrincipal("Naruto");

        when(animeRepository.findByTituloPrincipal("Naruto")).thenReturn(Optional.of(anime));

        Anime resultado = animeService.buscarPorTitulo("Naruto");

        assertNotNull(resultado);
        assertEquals("Naruto", resultado.getTituloPrincipal());
        verify(animeRepository, times(1)).findByTituloPrincipal("Naruto");
    }

    @Test
    void deveSalvarAnimeCorretamente() {
        Anime anime = new Anime();
        anime.setTituloPrincipal("One Piece");

        when(animeRepository.save(anime)).thenReturn(anime);

        Anime resultado = animeService.salvar(anime);

        assertNotNull(resultado);
        assertEquals("One Piece", resultado.getTituloPrincipal());
        verify(animeRepository, times(1)).save(anime);
    }

    @Test
    void deveRetornarTodosOsAnimes() {
        Anime a1 = new Anime();
        a1.setTituloPrincipal("Naruto");
        Anime a2 = new Anime();
        a2.setTituloPrincipal("One Piece");

        when(animeRepository.findAll()).thenReturn(Arrays.asList(a1, a2));

        List<Anime> resultado = animeService.listarTodos();

        assertEquals(2, resultado.size());
        verify(animeRepository, times(1)).findAll();
    }

    @Test
    void deveBuscarAnimesDaTemporadaAtualQuandoCacheNaoExiste() {
        String temporada = TemporadaUtil.getTemporadaAtual();
        int ano = LocalDate.now().getYear();

        when(temporadaCacheRepository.findByAnoAndTemporada(ano, temporada)).thenReturn(null);

        Anime anime = new Anime();
        anime.setTituloPrincipal("Attack on Titan");
        when(jikanClient.buscarAnimesTemporada(ano, temporada)).thenReturn(List.of(anime));

        List<Anime> resultado = animeService.carregarAnimesTemporadaAtual();

        assertEquals(1, resultado.size());
        assertEquals("Attack on Titan", resultado.get(0).getTituloPrincipal());
        verify(animeRepository, times(1)).saveAll(anyList());
        verify(temporadaCacheRepository, times(1)).save(any(TemporadaCache.class));
    }

    @Test
    void deveRetornarAnimesDaCacheQuandoExistir() {
        String temporada = TemporadaUtil.getTemporadaAtual();
        int ano = LocalDate.now().getYear();

        TemporadaCache cache = new TemporadaCache(temporada, ano);
        when(temporadaCacheRepository.findByAnoAndTemporada(ano, temporada)).thenReturn(cache);

        Anime a1 = new Anime();
        a1.setTituloPrincipal("Naruto");
        when(animeRepository.findAll()).thenReturn(List.of(a1));

        List<Anime> resultado = animeService.carregarAnimesTemporadaAtual();

        assertEquals(1, resultado.size());
        assertEquals("Naruto", resultado.get(0).getTituloPrincipal());
        verify(animeRepository, times(1)).findAll();
        verify(jikanClient, never()).buscarAnimesTemporada(anyInt(), anyString());
    }
}
