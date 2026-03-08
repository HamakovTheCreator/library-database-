package com.example.demo.repository;

import com.example.demo.model.Book;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.List;

@Repository
public interface BookRepository extends JpaRepository<Book, Long> {
    // Найти книги по автору
    List<Book> findByAuthor(String author);

    // Найти доступные книги
    List<Book> findByAvailableTrue();

    // Найти книгу по ISBN
    Book findByIsbn(String isbn);

    // Поиск по названию (без учета регистра)
    List<Book> findByTitleContainingIgnoreCase(String title);


}

