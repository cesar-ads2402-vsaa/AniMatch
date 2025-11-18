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
import java.util.ArrayList;
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

    @Test
    void deveBuscarNaApiQuandoNaoEncontrarPorIdNoBanco() {
        Long id = 1L;
        Anime animeDaApi = new Anime();
        animeDaApi.setId(id);
        animeDaApi.setTituloPrincipal("Naruto");

        when(animeRepository.findById(id)).thenReturn(Optional.empty());
        when(jikanClient.buscarAnimePorId(id)).thenReturn(animeDaApi);
        when(animeRepository.save(animeDaApi)).thenReturn(animeDaApi);

        Anime resultado = animeService.buscarPorId(id);

        assertNotNull(resultado);
        assertEquals("Naruto", resultado.getTituloPrincipal());
        verify(animeRepository, times(1)).findById(id);
        verify(jikanClient, times(1)).buscarAnimePorId(id);
        verify(animeRepository, times(1)).save(animeDaApi);
    }

    @Test
    void deveRetornarNullQuandoNaoEncontrarPorIdNemNaApi() {
        Long id = 1L;

        when(animeRepository.findById(id)).thenReturn(Optional.empty());
        when(jikanClient.buscarAnimePorId(id)).thenReturn(null);

        Anime resultado = animeService.buscarPorId(id);

        assertNull(resultado);
        verify(animeRepository, times(1)).findById(id);
        verify(jikanClient, times(1)).buscarAnimePorId(id);
        verify(animeRepository, never()).save(any());
    }

    @Test
    void deveBuscarNaApiQuandoNaoEncontrarPorTituloNoBanco() {
        String titulo = "Naruto";
        Anime animeDaApi = new Anime();
        animeDaApi.setId(1L);
        animeDaApi.setTituloPrincipal("Naruto");

        when(animeRepository.findByTituloPrincipal(titulo)).thenReturn(Optional.empty());
        when(jikanClient.buscarPorTitulo(titulo)).thenReturn(List.of(animeDaApi));
        when(animeRepository.save(animeDaApi)).thenReturn(animeDaApi);

        Anime resultado = animeService.buscarPorTitulo(titulo);

        assertNotNull(resultado);
        assertEquals("Naruto", resultado.getTituloPrincipal());
        verify(animeRepository, times(1)).findByTituloPrincipal(titulo);
        verify(jikanClient, times(1)).buscarPorTitulo(titulo);
        verify(animeRepository, times(1)).save(animeDaApi);
    }

    @Test
    void deveRetornarNullQuandoNaoEncontrarPorTituloNemNaApi() {
        String titulo = "AnimeInexistente";

        when(animeRepository.findByTituloPrincipal(titulo)).thenReturn(Optional.empty());
        when(jikanClient.buscarPorTitulo(titulo)).thenReturn(new ArrayList<>());

        Anime resultado = animeService.buscarPorTitulo(titulo);

        assertNull(resultado);
        verify(animeRepository, times(1)).findByTituloPrincipal(titulo);
        verify(jikanClient, times(1)).buscarPorTitulo(titulo);
        verify(animeRepository, never()).save(any());
    }

    @Test
    void deveRetornarResultadosDoBancoQuandoBuscarComFiltros() {
        String genero = "Action";
        String classificacao = "PG-13";
        String status = "Finished Airing";
        String palavraChave = null;

        Anime anime1 = new Anime();
        anime1.setTituloPrincipal("Naruto");
        List<Anime> resultados = List.of(anime1);

        when(animeRepository.findByFiltros(genero, classificacao, status, palavraChave))
                .thenReturn(resultados);

        List<Anime> resultado = animeService.buscarAnimesComFiltros(genero, classificacao, status, palavraChave);

        assertEquals(1, resultado.size());
        assertEquals("Naruto", resultado.get(0).getTituloPrincipal());
        verify(animeRepository, times(1)).findByFiltros(genero, classificacao, status, palavraChave);
        verify(jikanClient, never()).buscarPorTitulo(anyString());
    }

    @Test
    void deveBuscarNaApiQuandoNaoEncontrarComFiltrosNoBanco() {
        String genero = null;
        String classificacao = null;
        String status = null;
        String palavraChave = "Naruto";

        Anime animeDaApi = new Anime();
        animeDaApi.setId(1L);
        animeDaApi.setTituloPrincipal("Naruto");

        when(animeRepository.findByFiltros(genero, classificacao, status, palavraChave))
                .thenReturn(new ArrayList<>());
        when(jikanClient.buscarPorTitulo(palavraChave)).thenReturn(List.of(animeDaApi));
        when(animeRepository.saveAll(anyList())).thenReturn(List.of(animeDaApi));

        List<Anime> resultado = animeService.buscarAnimesComFiltros(genero, classificacao, status, palavraChave);

        assertEquals(1, resultado.size());
        assertEquals("Naruto", resultado.get(0).getTituloPrincipal());
        verify(animeRepository, times(1)).findByFiltros(genero, classificacao, status, palavraChave);
        verify(jikanClient, times(1)).buscarPorTitulo(palavraChave);
        verify(animeRepository, times(1)).saveAll(anyList());
    }

    @Test
    void deveRetornarListaVaziaQuandoNaoEncontrarComFiltrosESemPalavraChave() {
        String genero = "Action";
        String classificacao = null;
        String status = null;
        String palavraChave = null;

        when(animeRepository.findByFiltros(genero, classificacao, status, palavraChave))
                .thenReturn(new ArrayList<>());

        List<Anime> resultado = animeService.buscarAnimesComFiltros(genero, classificacao, status, palavraChave);

        assertTrue(resultado.isEmpty());
        verify(animeRepository, times(1)).findByFiltros(genero, classificacao, status, palavraChave);
        verify(jikanClient, never()).buscarPorTitulo(anyString());
    }

    @Test
    void deveRetornarListaVaziaQuandoApiRetornarNullNaBuscaComFiltros() {
        String genero = null;
        String classificacao = null;
        String status = null;
        String palavraChave = "AnimeInexistente";

        when(animeRepository.findByFiltros(genero, classificacao, status, palavraChave))
                .thenReturn(new ArrayList<>());
        when(jikanClient.buscarPorTitulo(palavraChave)).thenReturn(null);

        List<Anime> resultado = animeService.buscarAnimesComFiltros(genero, classificacao, status, palavraChave);

        assertNotNull(resultado);
        assertTrue(resultado.isEmpty());
        verify(animeRepository, times(1)).findByFiltros(genero, classificacao, status, palavraChave);
        verify(jikanClient, times(1)).buscarPorTitulo(palavraChave);
        verify(animeRepository, never()).saveAll(anyList());
    }

    @Test
    void deveBuscarPorGenero() {
        String genero = "Action";
        Anime anime = new Anime();
        anime.setTituloPrincipal("Naruto");

        when(animeRepository.findByGenerosContaining(genero)).thenReturn(List.of(anime));

        List<Anime> resultado = animeService.buscarPorGenero(genero);

        assertEquals(1, resultado.size());
        assertEquals("Naruto", resultado.get(0).getTituloPrincipal());
        verify(animeRepository, times(1)).findByGenerosContaining(genero);
    }

    @Test
    void deveBuscarPorClassificacao() {
        String classificacao = "PG-13";
        Anime anime = new Anime();
        anime.setTituloPrincipal("Naruto");

        when(animeRepository.findByClassificacao(classificacao)).thenReturn(List.of(anime));

        List<Anime> resultado = animeService.buscarPorClassificacao(classificacao);

        assertEquals(1, resultado.size());
        assertEquals("Naruto", resultado.get(0).getTituloPrincipal());
        verify(animeRepository, times(1)).findByClassificacao(classificacao);
    }

    @Test
    void deveBuscarPorStatus() {
        String status = "Finished Airing";
        Anime anime = new Anime();
        anime.setTituloPrincipal("Naruto");

        when(animeRepository.findByStatus(status)).thenReturn(List.of(anime));

        List<Anime> resultado = animeService.buscarPorStatus(status);

        assertEquals(1, resultado.size());
        assertEquals("Naruto", resultado.get(0).getTituloPrincipal());
        verify(animeRepository, times(1)).findByStatus(status);
    }
}
