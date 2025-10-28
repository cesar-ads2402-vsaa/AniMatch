package com.example.animematch.controller;

import com.example.animematch.model.Anime;
import com.example.animematch.service.AnimeService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

class AnimeControllerTest {

private MockMvc mockMvc;

@Mock
private AnimeService animeService;

@InjectMocks
private AnimeController animeController;

private ObjectMapper objectMapper;

@BeforeEach
void setUp() {
    MockitoAnnotations.openMocks(this);
    mockMvc = MockMvcBuilders.standaloneSetup(animeController).build();
    objectMapper = new ObjectMapper();
}

@Test
void deveListarTodosOsAnimes() throws Exception {
    Anime a1 = new Anime();
    a1.setTituloPrincipal("Naruto");
    a1.setClassificacao("PG-13 - Teens 13 or older");
    Anime a2 = new Anime();
    a2.setTituloPrincipal("One Piece");
    a2.setClassificacao("PG - Children");

    when(animeService.listarTodos()).thenReturn(List.of(a1, a2));

    mockMvc.perform(get("/api/animes"))
            .andExpect(status().isOk())
            .andExpect(jsonPath("$.length()").value(2))
            .andExpect(jsonPath("$[0].tituloPrincipal").value("Naruto"))
            .andExpect(jsonPath("$[1].tituloPrincipal").value("One Piece"));
}

@Test
void deveBuscarAnimePorId() throws Exception {
    Anime anime = new Anime();
    anime.setId(1L);
    anime.setTituloPrincipal("Naruto");
    anime.setClassificacao("PG-13 - Teens 13 or older");

    when(animeService.buscarPorId(1L)).thenReturn(anime);

    mockMvc.perform(get("/api/animes/1"))
            .andExpect(status().isOk())
            .andExpect(jsonPath("$.tituloPrincipal").value("Naruto"));
}

@Test
void deveSalvarAnime() throws Exception {
    Anime anime = new Anime();
    anime.setTituloPrincipal("Bleach");
    anime.setClassificacao("R - 17+ (violence & profanity)");

    when(animeService.salvar(any(Anime.class))).thenReturn(anime);

    mockMvc.perform(post("/api/animes")
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(objectMapper.writeValueAsString(anime)))
            .andExpect(status().isOk())
            .andExpect(jsonPath("$.tituloPrincipal").value("Bleach"));
}

@Test
void deveListarAnimesDaTemporadaAtual() throws Exception {
    Anime anime = new Anime();
    anime.setTituloPrincipal("Attack on Titan");
    anime.setClassificacao("R - 17+ (violence & profanity)");

    when(animeService.carregarAnimesTemporadaAtual()).thenReturn(List.of(anime));

    mockMvc.perform(get("/api/animes/temporada/atual"))
            .andExpect(status().isOk())
            .andExpect(jsonPath("$[0].tituloPrincipal").value("Attack on Titan"));
}

@Test
void deveBuscarAnimesComFiltros() throws Exception {
    Anime anime = new Anime();
    anime.setTituloPrincipal("Demon Slayer");
    anime.setClassificacao("PG-13 - Teens 13 or older");

    when(animeService.buscarAnimesComFiltros("Ação", "PG-13", "Em exibição", "Demon"))
            .thenReturn(List.of(anime));

    mockMvc.perform(get("/api/animes/buscar")
                    .param("genero", "Ação")
                    .param("classificacao", "PG-13")
                    .param("status", "Em exibição")
                    .param("palavraChave", "Demon"))
            .andExpect(status().isOk())
            .andExpect(jsonPath("$[0].tituloPrincipal").value("Demon Slayer"));
}

@Test
void deveFiltrarAnimesComClassificacaoProibida() throws Exception {
    Anime animePermitido = new Anime();
    animePermitido.setTituloPrincipal("Naruto");
    animePermitido.setClassificacao("PG-13 - Teens 13 or older");
    
    Anime animeProibido = new Anime();
    animeProibido.setTituloPrincipal("Anime Proibido");
    animeProibido.setClassificacao("Rx - Hentai");
    
    
    when(animeService.listarTodos())
        .thenReturn(List.of(animePermitido, animeProibido));
    
    
    mockMvc.perform(get("/api/animes"))
            .andExpect(status().isOk())
            .andExpect(jsonPath("$.length()").value(1))
            .andExpect(jsonPath("$[0].tituloPrincipal").value("Naruto"));
}

}
