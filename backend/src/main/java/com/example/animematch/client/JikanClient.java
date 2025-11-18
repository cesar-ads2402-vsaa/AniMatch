package com.example.animematch.client;

import com.example.animematch.model.Anime;
import com.example.animematch.model.Imagens;
import com.example.animematch.model.Periodo;
import com.example.animematch.util.ClassificacaoUtil;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import com.example.animematch.model.Review;
import org.springframework.web.util.UriComponentsBuilder;

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

    public JikanClient(RestTemplate restTemplate) {
        this.restTemplate = restTemplate;
    }

    public List<Anime> buscarAnimesTemporada(int ano, String temporada) {
        List<Anime> todosAnimes = new ArrayList<>();
        int paginaAtual = 1;
        boolean temProximaPagina = true;

        try {
            while (temProximaPagina) {
                String url = BASE_URL + "/seasons/" + ano + "/" + temporada + "?page=" + paginaAtual;

                Map<String, Object> response = null;
                try {
                    response = restTemplate.getForObject(url, Map.class);
                } catch (Exception e) {
                    System.err.println("Erro ao buscar página " + paginaAtual + " da temporada " + temporada + " " + ano + ": " + e.getMessage());
                    break;
                }

                if (response == null) {
                    break;
                }

                List<Map<String, Object>> data = (List<Map<String, Object>>) response.get("data");
                if (data != null && !data.isEmpty()) {
                    List<Anime> animesDaPagina = processarListaDeAnimes(data);
                    todosAnimes.addAll(animesDaPagina);
                }

                Map<String, Object> pagination = (Map<String, Object>) response.get("pagination");
                if (pagination != null) {
                    Boolean hasNextPage = (Boolean) pagination.get("has_next_page");
                    temProximaPagina = Boolean.TRUE.equals(hasNextPage);
                } else {
                    temProximaPagina = false;
                }

                paginaAtual++;
            }
        } catch (Exception e) {
            System.err.println("Erro ao buscar animes da temporada " + temporada + " " + ano + ": " + e.getMessage());
            e.printStackTrace();
        }

        return todosAnimes;
    }

    public List<Anime> buscarPorTitulo(String titulo) {
        try {
            String url = UriComponentsBuilder
                    .fromHttpUrl(BASE_URL + "/anime")
                    .queryParam("q", titulo)
                    .queryParam("limit", 15)
                    .toUriString();

            Map<String, Object> response = restTemplate.getForObject(url, Map.class);
            List<Map<String, Object>> data = response != null
                    ? (List<Map<String, Object>>) response.get("data")
                    : null;

            return processarListaDeAnimes(data);
        } catch (Exception e) {
            System.err.println("Erro ao buscar animes por título '" + titulo + "': " + e.getMessage());
            e.printStackTrace();
            return new ArrayList<>();
        }
    }

    public Anime buscarAnimePorId(Long animeId) {
        try {
            String url = BASE_URL + "/anime/" + animeId;

            Map<String, Object> response = restTemplate.getForObject(url, Map.class);
            if (response == null) {
                System.err.println("Resposta nula da API Jikan para anime ID: " + animeId);
                return null;
            }

            Map<String, Object> data = (Map<String, Object>) response.get("data");
            if (data == null) {
                System.err.println("Data nulo na resposta da API Jikan para anime ID: " + animeId);
                return null;
            }

            return mapearAnime(data);
        } catch (Exception e) {
            System.err.println("Erro ao buscar anime por ID " + animeId + " na API Jikan: " + e.getMessage());
            e.printStackTrace();
            return null;
        }
    }

    private List<Anime> processarListaDeAnimes(List<Map<String, Object>> data) {
        List<Anime> animes = new ArrayList<>();

        if (data == null) {
            return animes;
        }

        for (Map<String, Object> item : data) {
            try {
                Anime anime = mapearAnime(item);
                if (anime != null) {
                    animes.add(anime);
                }
            } catch (Exception e) {
                System.err.println("Erro ao processar o item: " + item.get("title"));
                e.printStackTrace();
            }
        }

        return animes;
    }

    private Anime mapearAnime(Map<String, Object> item) {
        if (item == null) {
            return null;
        }

        Integer id = (Integer) item.get("mal_id");
        if (id == null) {
            return null;
        }

        String tituloPrincipal = (String) item.get("title");
        String status = (String) item.get("status");
        String classificacao = (String) item.get("rating");
        if (ClassificacaoUtil.ehClassificacaoProibida(classificacao)) {
            System.out.println("Anime filtrado (classificação proibida): " + tituloPrincipal + " - " + classificacao);
            return null;
        }

        Double nota = null;
        if (item.get("score") != null) {
            nota = ((Number) item.get("score")).doubleValue();
        }

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
            Object fromObj = aired.get("from");
            if (fromObj instanceof String from && from.length() >= 10) {
                dataInicio = LocalDate.parse(from.substring(0, 10));
            }
            Object toObj = aired.get("to");
            if (toObj instanceof String to && to.length() >= 10) {
                dataFim = LocalDate.parse(to.substring(0, 10));
            }
        }
        Periodo periodoExibicao = new Periodo(dataInicio, dataFim);

        Map<String, Object> imagensMap = (Map<String, Object>) item.get("images");
        Map<String, Object> jpg = imagensMap != null ? (Map<String, Object>) imagensMap.get("jpg") : null;
        String urlPequena = jpg != null ? (String) jpg.get("small_image_url") : null;
        String urlMedia = jpg != null ? (String) jpg.get("image_url") : null;
        String urlGrande = jpg != null ? (String) jpg.get("large_image_url") : null;
        Imagens imagens = new Imagens(urlPequena, urlMedia, urlGrande);

        return new Anime(
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
    }

    public List<Review> buscarReviewsDoAnime(Long animeId) {
        List<Review> reviews = new ArrayList<>();

        try {
            String url = BASE_URL + "/anime/" + animeId + "/reviews";

            Map<String, Object> response = restTemplate.getForObject(url, Map.class);
            if (response == null) {
                System.err.println("Resposta nula da API Jikan para reviews do anime ID: " + animeId);
                return reviews;
            }

            List<Map<String, Object>> data = (List<Map<String, Object>>) response.get("data");
            if (data == null) {
                return reviews;
            }

            for (Map<String, Object> item : data) {
                try {
                    Integer malId = (Integer) item.get("mal_id");
                    if (malId == null) {
                        continue;
                    }

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
                    e.printStackTrace();
                }
            }
        } catch (Exception e) {
            System.err.println("Erro ao buscar reviews do anime ID " + animeId + ": " + e.getMessage());
            e.printStackTrace();
        }

        return reviews;
    }
}
