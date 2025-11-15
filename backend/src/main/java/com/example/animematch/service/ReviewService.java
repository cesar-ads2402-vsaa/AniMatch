package com.example.animematch.service;

import com.example.animematch.model.Review;
import com.example.animematch.client.JikanClient;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ReviewService {

    private final JikanClient jikanClient;

    @Autowired
    public ReviewService(JikanClient jikanClient) {
        this.jikanClient = jikanClient;
    }
    
    public List<Review> buscarPorAnimeId(Long animeId) {
        return jikanClient.buscarReviewsDoAnime(animeId);
    }
    
}