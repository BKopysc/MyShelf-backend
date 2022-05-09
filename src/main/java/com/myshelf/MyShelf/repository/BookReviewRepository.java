package com.myshelf.MyShelf.repository;

import com.myshelf.MyShelf.models.BookReview;
import org.springframework.data.jpa.repository.JpaRepository;

public interface BookReviewRepository  extends JpaRepository<BookReview, Long> {
}
