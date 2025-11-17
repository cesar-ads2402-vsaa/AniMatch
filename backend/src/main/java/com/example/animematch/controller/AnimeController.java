package com.example.animematch.controller;

import com.example.animematch.model.Anime;
import com.example.animematch.model.Review;
import com.example.animematch.service.AnimeService;
import com.example.animematch.service.ReviewService;
import com.example.animematch.util.ClassificacaoUtil;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/animes")
public class AnimeController {

private final AnimeService animeService;
    private final ReviewService reviewService;

   
public AnimeController(AnimeService animeService, ReviewService reviewService) { 
this.animeService = animeService;
        this.reviewService = reviewService;
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
 
   
    @GetMapping("/{animeId}/reviews")
    public List<Review> getReviewsDoAnime(@PathVariable Long animeId) {
     
        return reviewService.buscarPorAnimeId(animeId);
    }
    
private List<Anime> filtrarAnimesInadequados(List<Anime> animes) {
return animes.stream()
.filter(anime -> !ClassificacaoUtil.ehClassificacaoProibida(anime.getClassificacao()))
.collect(Collectors.toList());
}
}