package com.myshelf.MyShelf.repository;

import com.myshelf.MyShelf.models.Film;
import org.springframework.data.jpa.repository.JpaRepository;

public interface FilmRepository extends JpaRepository<Film, Long> {
}
