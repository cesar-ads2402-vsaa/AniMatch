package com.example.animematch.repository;

import com.example.animematch.model.Review;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ReviewRepository extends JpaRepository<Review, Long> {

    List<Review> findByAnimeId(Long animeId);

}