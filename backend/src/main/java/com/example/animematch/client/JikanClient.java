package com.example.animematch.client;

import com.example.animematch.model.Anime;
import com.example.animematch.model.Imagens;
import com.example.animematch.model.Periodo;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.time.LocalDate;
import java.util.*;
import java.util.stream.Collectors;

@Service
public class JikanClient {

    private final RestTemplate restTemplate;
    private static final String BASE_URL = "https://api.jikan.moe/v4";

    public JikanClient() {
        this.restTemplate = new RestTemplate();
    }

    public List<Anime> buscarAnimesTemporada(int ano, String temporada) {
        String url = BASE_URL + "/seasons/" + ano + "/" + temporada;

        Map<String, Object> response = restTemplate.getForObject(url, Map.class);
        
        List<Map<String, Object>> data = (List<Map<String, Object>>) response.get("data");

        List<Anime> animes = new ArrayList<>();

        if (data == null) {
            return animes;
        }

        for (Map<String, Object> item : data) {
            try {
                // ID
                Integer id = (Integer) item.get("mal_id");

                // Título
                String tituloPrincipal = (String) item.get("title");

                // Status
                String status = (String) item.get("status");

                // Classificação indicativa
                String classificacao = (String) item.get("rating");

                // Nota
                Double nota = item.get("score") != null ? ((Number) item.get("score")).doubleValue() : null;

                // Episódios
                Integer episodios = item.get("episodes") != null ? (Integer) item.get("episodes") : 0;

                // Sinopse
                String sinopse = (String) item.get("synopsis");

                // Trailer
                Map<String, Object> trailer = (Map<String, Object>) item.get("trailer");
                String urlTrailer = trailer != null ? (String) trailer.get("url") : null;

                // Ano
                int anoDeLancamento = item.get("year") != null ? (Integer) item.get("year") : 0;

                // Popularidade
                int popularidade = item.get("popularity") != null ? (Integer) item.get("popularity") : 0;

                // Gêneros
                List<Map<String, Object>> generosJson = (List<Map<String, Object>>) item.get("genres");
                List<String> generos = generosJson != null
                        ? generosJson.stream().map(g -> (String) g.get("name")).collect(Collectors.toList())
                        : Collections.emptyList();

                // Estúdios
                List<Map<String, Object>> estudiosJson = (List<Map<String, Object>>) item.get("studios");
                List<String> estudios = estudiosJson != null
                        ? estudiosJson.stream().map(e -> (String) e.get("name")).collect(Collectors.toList())
                        : Collections.emptyList();

                // Reviews
                List<String> reviews = new ArrayList<>();

                // Período de exibição
                Map<String, Object> aired = (Map<String, Object>) item.get("aired");
                LocalDate dataInicio = null;
                LocalDate dataFim = null;
                if (aired != null) {
                    if (aired.get("from") instanceof String) {
                        dataInicio = LocalDate.parse(((String) aired.get("from")).substring(0, 10));
                    }
                    if (aired.get("to") instanceof String) {
                        dataFim = LocalDate.parse(((String) aired.get("to")).substring(0, 10));
                    }
                }
                Periodo periodoExibicao = new Periodo(dataInicio, dataFim);

                // Imagens
                Map<String, Object> imagensMap = (Map<String, Object>) item.get("images");
                Map<String, Object> jpg = (Map<String, Object>) imagensMap.get("jpg");
                String urlPequena = jpg != null ? (String) jpg.get("small_image_url") : null;
                String urlMedia = jpg != null ? (String) jpg.get("image_url") : null;
                String urlGrande = jpg != null ? (String) jpg.get("large_image_url") : null;
                Imagens imagens = new Imagens(urlPequena, urlMedia, urlGrande);

                // Criar Anime
                Anime anime = new Anime(
                        id.longValue(),
                        tituloPrincipal,
                        status,
                        classificacao,
                        nota,
                        episodios,
                        sinopse,
                        urlTrailer,
                        anoDeLancamento,
                        popularidade,
                        generos,
                        estudios,
                        reviews,
                        periodoExibicao,
                        imagens
                );
                animes.add(anime);

            } catch (Exception e) {
                System.err.println("Erro ao processar o item: " + item.get("title"));
                e.printStackTrace();
            }
        }
        return animes;
    }
}