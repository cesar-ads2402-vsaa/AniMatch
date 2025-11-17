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

    /**
     * Ao subir a aplicação, carrega os animes da temporada atual
     * (apenas na primeira vez em que essa temporada for carregada).
     */
    @Override
    public void run(String... args) {
        carregarAnimesTemporadaAtual();
    }

    /**
     * Busca animes da temporada atual na Jikan e salva no banco,
     * se ainda não houver cache para essa temporada.
     */
    public List<Anime> carregarAnimesTemporadaAtual() {
        String temporadaAtual = TemporadaUtil.getTemporadaAtual();
        int anoAtual = LocalDate.now().getYear();

        TemporadaCache cache = temporadaCacheRepository.findByAnoAndTemporada(anoAtual, temporadaAtual);

        if (cache != null) {
            System.out.println("Temporada já carregada no banco: " + temporadaAtual + " " + anoAtual);
            // Já está carregado: apenas devolve o que tiver no banco
            return animeRepository.findAll();
        }

        System.out.println("Nenhum cache encontrado. Buscando da Jikan API...");

        List<Anime> animesDaTemporada = jikanClient.buscarAnimesTemporada(anoAtual, temporadaAtual);

        animeRepository.saveAll(animesDaTemporada);

        temporadaCacheRepository.save(new TemporadaCache(temporadaAtual, anoAtual));

        System.out.println("Temporada " + temporadaAtual + " " + anoAtual + " carregada com sucesso!");

        return animesDaTemporada;
    }

    /**
     * Listagem geral (vai mostrar, inicialmente, os animes da temporada carregados
     * + qualquer outro anime que você eventualmente salvar no banco).
     */
    public List<Anime> listarTodos() {
        return animeRepository.findAll();
    }

    public Anime salvar(Anime anime) {
        return animeRepository.save(anime);
    }

    public Anime buscarPorId(Long id) {
        return animeRepository.findById(id).orElse(null);
    }

    /**
     * Busca por título:
     *  - Primeiro tenta no banco (temporada + o que já foi cacheado).
     *  - Se não encontrar, tenta na Jikan.
     */
    public Anime buscarPorTitulo(String titulo) {
        Anime doBanco = animeRepository.findByTituloPrincipal(titulo).orElse(null);
        if (doBanco != null) {
            return doBanco;
        }

        // Fallback na Jikan
        List<Anime> daApi = jikanClient.buscarPorTitulo(titulo);
        if (daApi == null || daApi.isEmpty()) {
            return null;
        }

        // Se quiser cachear o primeiro resultado:
        // Anime salvo = animeRepository.save(daApi.get(0));
        // return salvo;

        return daApi.get(0);
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

    /**
     * Busca com filtros:
     *  - Primeiro tenta no banco com os filtros.
     *  - Se não achar nada e existir palavra-chave, consulta a Jikan.
     *    Isso permite que o usuário encontre animes fora da temporada.
     */
    public List<Anime> buscarAnimesComFiltros(String genero,
                                              String classificacao,
                                              String status,
                                              String palavraChave) {

        // 1. Tenta primeiro no banco (animes da temporada / cache local)
        List<Anime> resultados = animeRepository.findByFiltros(
                genero,
                classificacao,
                status,
                palavraChave
        );

        if (!resultados.isEmpty()) {
            return resultados;
        }

        // 2. Se não achou nada no banco e o usuário informou um nome,
        //    chama a Jikan para buscar diretamente lá.
        if (palavraChave != null && !palavraChave.isBlank()) {
            List<Anime> daApi = jikanClient.buscarPorTitulo(palavraChave);

            // Se quiser, pode salvar o que vier no banco para cache:
            // animeRepository.saveAll(daApi);

            return daApi;
        }

        // 3. Sem resultados e sem palavra-chave, devolve a lista vazia mesmo
        return resultados;
    }
}
