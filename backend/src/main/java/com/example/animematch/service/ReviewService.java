package com.example.animematch.service;

import com.example.animematch.model.Review;
import com.example.animematch.repository.ReviewRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ReviewService {

    private final ReviewRepository reviewRepository;

    @Autowired
    public ReviewService(ReviewRepository reviewRepository) {
        this.reviewRepository = reviewRepository;
    }
    public List<Review> buscarPorAnimeId(Long animeId) {
        return reviewRepository.findByAnimeId(animeId);
    }
    
}