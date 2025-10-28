package com.example.animematch.controller;

import com.example.animematch.model.Anime;
import com.example.animematch.service.AnimeService;
import com.example.animematch.util.ClassificacaoUtil;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/animes")
public class AnimeController {

    private final AnimeService animeService;

    public AnimeController(AnimeService animeService) {
        this.animeService = animeService;
    }

    @GetMapping
    public List<Anime> listarAnimes() {
        return filtrarAnimesInadequados(animeService.listarTodos());
    }

    @GetMapping("/{id}")
    public Anime buscarAnime(@PathVariable Long id) {
        return animeService.buscarPorId(id);
    }

    @PostMapping
    public Anime salvarAnime(@RequestBody Anime anime) {
        return animeService.salvar(anime);
    }

    @GetMapping("/temporada/atual")
    public List<Anime> listarAnimesTemporadaAtual() {
        return filtrarAnimesInadequados(animeService.carregarAnimesTemporadaAtual());
    }

    @GetMapping("/buscar")
    public List<Anime> buscarAnimesComFiltros(
            @RequestParam(required = false) String genero,
            @RequestParam(required = false) String classificacao,
            @RequestParam(required = false) String status,
            @RequestParam(required = false) String palavraChave) {
        return filtrarAnimesInadequados(animeService.buscarAnimesComFiltros(genero, classificacao, status, palavraChave));
    }
    
    
    private List<Anime> filtrarAnimesInadequados(List<Anime> animes) {
        return animes.stream()
                .filter(anime -> !ClassificacaoUtil.ehClassificacaoProibida(anime.getClassificacao()))
                .collect(Collectors.toList());
    }
}