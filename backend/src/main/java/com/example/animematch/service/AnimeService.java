package com.example.animematch.service;

import com.example.animematch.client.JikanClient;
import com.example.animematch.model.Anime;
import com.example.animematch.model.TemporadaCache;
import com.example.animematch.repository.AnimeRepository;
import com.example.animematch.repository.TemporadaCacheRepository;
import com.example.animematch.util.TemporadaUtil;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class AnimeService implements CommandLineRunner {

    private final AnimeRepository animeRepository;
    private final TemporadaCacheRepository temporadaCacheRepository;
    private final JikanClient jikanClient;

    public AnimeService(AnimeRepository animeRepository,
                        TemporadaCacheRepository temporadaCacheRepository,
                        JikanClient jikanClient) {
        this.animeRepository = animeRepository;
        this.temporadaCacheRepository = temporadaCacheRepository;
        this.jikanClient = jikanClient;
    }

    @Override
    public void run(String... args) {
        carregarAnimesTemporadaAtual();
    }

    public List<Anime> carregarAnimesTemporadaAtual() {
        String temporadaAtual = TemporadaUtil.getTemporadaAtual();
        int anoAtual = LocalDate.now().getYear();

        TemporadaCache cache = temporadaCacheRepository.findByAnoAndTemporada(anoAtual, temporadaAtual);

        if (cache != null) {
            System.out.println("Temporada já carregada no banco: " + temporadaAtual + " " + anoAtual);
            return filtrarAnimesPorTemporada(animeRepository.findAll(), anoAtual, temporadaAtual);
        }

        System.out.println("Nenhum cache encontrado. Buscando da Jikan API...");

        List<Anime> animesDaTemporada = jikanClient.buscarAnimesTemporada(anoAtual, temporadaAtual);

        animeRepository.saveAll(animesDaTemporada);

        temporadaCacheRepository.save(new TemporadaCache(temporadaAtual, anoAtual));

        System.out.println("Temporada " + temporadaAtual + " " + anoAtual + " carregada com sucesso!");

        return animesDaTemporada;
    }

    private List<Anime> filtrarAnimesPorTemporada(List<Anime> todosAnimes, int ano, String temporada) {
        LocalDate inicioTemporada = getInicioTemporada(ano, temporada);
        LocalDate fimTemporada = getFimTemporada(ano, temporada);

        return todosAnimes.stream()
                .filter(anime -> {
                    if (anime.getPeriodoExibicao() == null) {
                        return false;
                    }
                    LocalDate dataInicio = anime.getPeriodoExibicao().getDataInicio();
                    if (dataInicio == null) {
                        return false;
                    }
                    return !dataInicio.isBefore(inicioTemporada) && !dataInicio.isAfter(fimTemporada);
                })
                .collect(Collectors.toList());
    }

    private LocalDate getInicioTemporada(int ano, String temporada) {
        switch (temporada.toLowerCase()) {
            case "winter":
                return LocalDate.of(ano, 1, 1);
            case "spring":
                return LocalDate.of(ano, 4, 1);
            case "summer":
                return LocalDate.of(ano, 7, 1);
            case "fall":
                return LocalDate.of(ano, 10, 1);
            default:
                return LocalDate.of(ano, 1, 1);
        }
    }

    private LocalDate getFimTemporada(int ano, String temporada) {
        switch (temporada.toLowerCase()) {
            case "winter":
                return LocalDate.of(ano, 3, 31);
            case "spring":
                return LocalDate.of(ano, 6, 30);
            case "summer":
                return LocalDate.of(ano, 9, 30);
            case "fall":
                return LocalDate.of(ano, 12, 31);
            default:
                return LocalDate.of(ano, 12, 31);
        }
    }

    public List<Anime> listarTodos() {
        return animeRepository.findAll();
    }

    public Anime salvar(Anime anime) {
        return animeRepository.save(anime);
    }

    public Anime buscarPorId(Long id) {
        return animeRepository.findById(id)
                .orElseGet(() -> {
                    Anime animeDaApi = jikanClient.buscarAnimePorId(id);
                    if (animeDaApi != null) {
                        return animeRepository.save(animeDaApi);
                    }
                    return null;
                });
    }

    public Anime buscarPorTitulo(String titulo) {
        Anime doBanco = animeRepository.findByTituloPrincipal(titulo).orElse(null);
        if (doBanco != null) {
            return doBanco;
        }

        List<Anime> daApi = jikanClient.buscarPorTitulo(titulo);
        if (daApi == null || daApi.isEmpty()) {
            return null;
        }

        Anime salvo = animeRepository.save(daApi.get(0));
        return salvo;
    }

    public List<Anime> buscarPorGenero(String genero) {
        return animeRepository.findByGenerosContaining(genero);
    }

    public List<Anime> buscarPorClassificacao(String classificacao) {
        return animeRepository.findByClassificacao(classificacao);
    }

    public List<Anime> buscarPorStatus(String status) {
        return animeRepository.findByStatus(status);
    }

    public List<Anime> buscarAnimesComFiltros(String genero,
                                              String classificacao,
                                              String status,
                                              String palavraChave) {

        List<Anime> resultados = animeRepository.findByFiltros(
                genero,
                classificacao,
                status,
                palavraChave
        );

        if (!resultados.isEmpty()) {
            return resultados;
        }

        if (palavraChave != null && !palavraChave.isBlank()) {
            List<Anime> daApi = jikanClient.buscarPorTitulo(palavraChave);

            if (daApi != null && !daApi.isEmpty()) {
                animeRepository.saveAll(daApi);
            }

            return daApi != null ? daApi : new ArrayList<>();
        }

        return resultados;
    }
}
