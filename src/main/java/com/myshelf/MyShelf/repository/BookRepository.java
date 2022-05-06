package com.myshelf.MyShelf.repository;

import com.myshelf.MyShelf.models.Book;
import org.springframework.data.jpa.repository.JpaRepository;

public interface BookRepository extends JpaRepository<Book, Long> {
}
