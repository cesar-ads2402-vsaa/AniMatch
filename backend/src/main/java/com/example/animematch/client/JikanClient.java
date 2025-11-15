package com.example.animematch.client;

import com.example.animematch.model.Anime;
import com.example.animematch.model.Imagens;
import com.example.animematch.model.Periodo;
import com.example.animematch.util.ClassificacaoUtil;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import com.example.animematch.model.Review;

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
                
                Integer id = (Integer) item.get("mal_id");

                
                String tituloPrincipal = (String) item.get("title");

               
                String status = (String) item.get("status");

                
                String classificacao = (String) item.get("rating");

                
                if (ClassificacaoUtil.ehClassificacaoProibida(classificacao)) {
                    System.out.println("Anime filtrado (classificação proibida): " + tituloPrincipal + " - " + classificacao);
                    continue;
                }

               
                Double nota = item.get("score") != null ? ((Number) item.get("score")).doubleValue() : null;

                
                Integer episodios = item.get("episodes") != null ? (Integer) item.get("episodes") : 0;

                
                String sinopse = (String) item.get("synopsis");

                
                Map<String, Object> trailer = (Map<String, Object>) item.get("trailer");
                String urlTrailer = trailer != null ? (String) trailer.get("url") : null;

                
                int anoDeLancamento = item.get("year") != null ? (Integer) item.get("year") : 0;

                
                int popularidade = item.get("popularity") != null ? (Integer) item.get("popularity") : 0;

                
                List<Map<String, Object>> generosJson = (List<Map<String, Object>>) item.get("genres");
                List<String> generos = generosJson != null
                        ? generosJson.stream().map(g -> (String) g.get("name")).collect(Collectors.toList())
                        : Collections.emptyList();

                
                List<Map<String, Object>> estudiosJson = (List<Map<String, Object>>) item.get("studios");
                List<String> estudios = estudiosJson != null
                        ? estudiosJson.stream().map(e -> (String) e.get("name")).collect(Collectors.toList())
                        : Collections.emptyList();

                
                List<String> reviews = new ArrayList<>();

                
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

                
                Map<String, Object> imagensMap = (Map<String, Object>) item.get("images");
                Map<String, Object> jpg = (Map<String, Object>) imagensMap.get("jpg");
                String urlPequena = jpg != null ? (String) jpg.get("small_image_url") : null;
                String urlMedia = jpg != null ? (String) jpg.get("image_url") : null;
                String urlGrande = jpg != null ? (String) jpg.get("large_image_url") : null;
                Imagens imagens = new Imagens(urlPequena, urlMedia, urlGrande);

               
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

    public List<Review> buscarReviewsDoAnime(Long animeId) {
        String url = BASE_URL + "/anime/" + animeId + "/reviews";

        Map<String, Object> response = restTemplate.getForObject(url, Map.class);
        
        List<Map<String, Object>> data = (List<Map<String, Object>>) response.get("data");

        List<Review> reviews = new ArrayList<>();

        if (data == null) {
            return reviews;
        }

        for (Map<String, Object> item : data) {
            try {
                
                Integer malId = (Integer) item.get("mal_id");
                
                Map<String, Object> user = (Map<String, Object>) item.get("user");
                String username = (user != null) ? (String) user.get("username") : "Anônimo";
                
                String comment = (String) item.get("review");

                Review review = new Review();
                review.setId(malId.longValue());
                review.setAuthor(username);
                review.setComment(comment);
                
                reviews.add(review);

            } catch (Exception e) {
                System.err.println("Erro ao processar review item: " + e.getMessage());
            }
        }
        
        return reviews;
    }
}