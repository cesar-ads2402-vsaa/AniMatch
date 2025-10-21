package com.example.animematch.service;

import com.example.animematch.model.Anime;
import com.example.animematch.model.TemporadaCache;
import com.example.animematch.repository.AnimeRepository;
import com.example.animematch.repository.TemporadaCacheRepository;
import com.example.animematch.client.JikanClient;
import com.example.animematch.util.TemporadaUtil;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;

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
            return animeRepository.findAll();
        }

        System.out.println("Nenhum cache encontrado. Buscando da Jikan API...");

        List<Anime> animesDaTemporada = jikanClient.buscarAnimesTemporada(anoAtual, temporadaAtual);

        animeRepository.saveAll(animesDaTemporada);

        temporadaCacheRepository.save(new TemporadaCache(temporadaAtual, anoAtual));

        System.out.println("Temporada " + temporadaAtual + " " + anoAtual + " carregada com sucesso!");

        return animesDaTemporada;
    }

    public List<Anime> listarTodos() {
        return animeRepository.findAll();
    }

    public Anime salvar(Anime anime) {
        return animeRepository.save(anime);
    }

    public Anime buscarPorId(Long id) {
        return animeRepository.findById(id).orElse(null);
    }

    public Anime buscarPorTitulo(String titulo) {
        return animeRepository.findByTituloPrincipal(titulo).orElse(null);
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

    public List<Anime> buscarAnimesComFiltros(String genero, String classificacao, String status, String palavraChave) {
        return animeRepository.findByFiltros(genero, classificacao, status, palavraChave);
    }
}