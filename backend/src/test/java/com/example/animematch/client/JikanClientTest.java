package com.example.animematch.client;

import com.example.animematch.model.Anime;
import com.example.animematch.model.Imagens;
import com.example.animematch.model.Periodo;
import com.example.animematch.util.ClassificacaoUtil;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockedStatic;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.web.client.RestClientException;
import org.springframework.web.client.RestTemplate;

import java.time.LocalDate;
import java.util.*;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class JikanClientTest {

    @Mock
    private RestTemplate restTemplate;

    @InjectMocks
    private JikanClient jikanClient;

    @BeforeEach
    void setUp() {
        jikanClient = new JikanClient(restTemplate);
    }

    @Test
    void deveBuscarAnimesTemporadaComPaginaUnica() {
        int ano = 2024;
        String temporada = "winter";

        Map<String, Object> response = criarRespostaTemporada(
                criarAnimeData(1L, "Anime 1"),
                criarAnimeData(2L, "Anime 2")
        );

        when(restTemplate.getForObject(anyString(), eq(Map.class))).thenReturn(response);

        List<Anime> resultado = jikanClient.buscarAnimesTemporada(ano, temporada);

        assertNotNull(resultado);
        verify(restTemplate, atLeastOnce()).getForObject(contains("/seasons/" + ano + "/" + temporada), eq(Map.class));
    }

    @Test
    void deveBuscarAnimesTemporadaComMultiplasPaginas() {
        int ano = 2024;
        String temporada = "winter";

        Map<String, Object> pagina1 = criarRespostaTemporadaComPaginacao(
                true,
                criarAnimeData(1L, "Anime 1"),
                criarAnimeData(2L, "Anime 2")
        );

        Map<String, Object> pagina2 = criarRespostaTemporadaComPaginacao(
                false,
                criarAnimeData(3L, "Anime 3"),
                criarAnimeData(4L, "Anime 4")
        );

        when(restTemplate.getForObject(contains("?page=1"), eq(Map.class))).thenReturn(pagina1);
        when(restTemplate.getForObject(contains("?page=2"), eq(Map.class))).thenReturn(pagina2);

        List<Anime> resultado = jikanClient.buscarAnimesTemporada(ano, temporada);

        assertNotNull(resultado);
        verify(restTemplate, times(1)).getForObject(contains("?page=1"), eq(Map.class));
        verify(restTemplate, times(1)).getForObject(contains("?page=2"), eq(Map.class));
    }

    @Test
    void deveTratarErroAoBuscarPagina() {
        int ano = 2024;
        String temporada = "winter";

        when(restTemplate.getForObject(anyString(), eq(Map.class)))
                .thenThrow(new RestClientException("Erro de conexão"));

        List<Anime> resultado = jikanClient.buscarAnimesTemporada(ano, temporada);

        assertNotNull(resultado);
        assertTrue(resultado.isEmpty());
    }

    @Test
    void deveRetornarListaVaziaQuandoRespostaNula() {
        int ano = 2024;
        String temporada = "winter";

        when(restTemplate.getForObject(anyString(), eq(Map.class))).thenReturn(null);

        List<Anime> resultado = jikanClient.buscarAnimesTemporada(ano, temporada);

        assertNotNull(resultado);
        assertTrue(resultado.isEmpty());
    }

    @Test
    void deveBuscarAnimesPorTitulo() {
        String titulo = "Naruto";

        Map<String, Object> response = criarRespostaTemporada(
                criarAnimeData(1L, "Naruto"),
                criarAnimeData(2L, "Naruto Shippuden")
        );

        when(restTemplate.getForObject(anyString(), eq(Map.class))).thenReturn(response);

        List<Anime> resultado = jikanClient.buscarPorTitulo(titulo);

        assertNotNull(resultado);
        verify(restTemplate, times(1)).getForObject(contains("/anime"), eq(Map.class));
    }

    @Test
    void deveRetornarListaVaziaQuandoErroAoBuscarPorTitulo() {
        String titulo = "Naruto";

        when(restTemplate.getForObject(anyString(), eq(Map.class)))
                .thenThrow(new RestClientException("Erro de conexão"));

        List<Anime> resultado = jikanClient.buscarPorTitulo(titulo);

        assertNotNull(resultado);
        assertTrue(resultado.isEmpty());
    }

    @Test
    void deveBuscarAnimePorId() {
        Long animeId = 1L;

        Map<String, Object> response = new HashMap<>();
        response.put("data", criarAnimeData(1L, "Naruto"));

        when(restTemplate.getForObject(contains("/anime/" + animeId), eq(Map.class))).thenReturn(response);

        Anime resultado = jikanClient.buscarAnimePorId(animeId);

        assertNotNull(resultado);
        verify(restTemplate, times(1)).getForObject(contains("/anime/" + animeId), eq(Map.class));
    }

    @Test
    void deveRetornarNullQuandoRespostaNulaAoBuscarPorId() {
        Long animeId = 1L;

        when(restTemplate.getForObject(anyString(), eq(Map.class))).thenReturn(null);

        Anime resultado = jikanClient.buscarAnimePorId(animeId);

        assertNull(resultado);
    }

    @Test
    void deveRetornarNullQuandoDataNuloAoBuscarPorId() {
        Long animeId = 1L;

        Map<String, Object> response = new HashMap<>();
        response.put("data", null);

        when(restTemplate.getForObject(anyString(), eq(Map.class))).thenReturn(response);

        Anime resultado = jikanClient.buscarAnimePorId(animeId);

        assertNull(resultado);
    }

    @Test
    void deveTratarErroAoBuscarAnimePorId() {
        Long animeId = 1L;

        when(restTemplate.getForObject(anyString(), eq(Map.class)))
                .thenThrow(new RestClientException("Erro de conexão"));

        Anime resultado = jikanClient.buscarAnimePorId(animeId);

        assertNull(resultado);
    }

    @Test
    void deveBuscarReviewsDoAnime() {
        Long animeId = 1L;

        Map<String, Object> response = new HashMap<>();
        List<Map<String, Object>> reviews = new ArrayList<>();
        
        Map<String, Object> review1 = new HashMap<>();
        review1.put("mal_id", 1);
        Map<String, Object> user1 = new HashMap<>();
        user1.put("username", "user1");
        review1.put("user", user1);
        review1.put("review", "Ótimo anime!");
        reviews.add(review1);

        response.put("data", reviews);

        when(restTemplate.getForObject(contains("/reviews"), eq(Map.class))).thenReturn(response);

        var resultado = jikanClient.buscarReviewsDoAnime(animeId);

        assertNotNull(resultado);
        verify(restTemplate, times(1)).getForObject(contains("/reviews"), eq(Map.class));
    }

    @Test
    void deveRetornarListaVaziaQuandoRespostaNulaAoBuscarReviews() {
        Long animeId = 1L;

        when(restTemplate.getForObject(anyString(), eq(Map.class))).thenReturn(null);

        var resultado = jikanClient.buscarReviewsDoAnime(animeId);

        assertNotNull(resultado);
        assertTrue(resultado.isEmpty());
    }

    @Test
    void deveTratarErroAoBuscarReviews() {
        Long animeId = 1L;

        when(restTemplate.getForObject(anyString(), eq(Map.class)))
                .thenThrow(new RestClientException("Erro de conexão"));

        var resultado = jikanClient.buscarReviewsDoAnime(animeId);

        assertNotNull(resultado);
        assertTrue(resultado.isEmpty());
    }

    private Map<String, Object> criarAnimeData(Long id, String titulo) {
        Map<String, Object> anime = new HashMap<>();
        anime.put("mal_id", id.intValue());
        anime.put("title", titulo);
        anime.put("status", "Finished Airing");
        anime.put("rating", "PG-13");
        anime.put("score", 8.5);
        anime.put("episodes", 24);
        anime.put("synopsis", "Sinopse do anime");
        anime.put("year", 2020);
        anime.put("popularity", 100);
        anime.put("genres", Collections.emptyList());
        anime.put("studios", Collections.emptyList());
        anime.put("aired", new HashMap<>());
        anime.put("images", new HashMap<>());
        anime.put("trailer", new HashMap<>());
        return anime;
    }

    private Map<String, Object> criarRespostaTemporada(Map<String, Object>... animes) {
        Map<String, Object> response = new HashMap<>();
        response.put("data", Arrays.asList(animes));
        Map<String, Object> pagination = new HashMap<>();
        pagination.put("has_next_page", false);
        response.put("pagination", pagination);
        return response;
    }

    private Map<String, Object> criarRespostaTemporadaComPaginacao(boolean hasNextPage, Map<String, Object>... animes) {
        Map<String, Object> response = new HashMap<>();
        response.put("data", Arrays.asList(animes));
        Map<String, Object> pagination = new HashMap<>();
        pagination.put("has_next_page", hasNextPage);
        response.put("pagination", pagination);
        return response;
    }
}

