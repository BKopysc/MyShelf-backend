package com.myshelf.MyShelf.repository;

import com.myshelf.MyShelf.models.FilmReview;
import org.springframework.data.jpa.repository.JpaRepository;

public interface FilmReviewRepository extends JpaRepository<FilmReview, Long> {
}
