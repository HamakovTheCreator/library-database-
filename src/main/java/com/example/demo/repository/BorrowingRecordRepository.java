package com.example.demo.repository;

import com.example.demo.model.BorrowingRecord;
import com.example.demo.model.Book;
import com.example.demo.model.Patron;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import java.time.LocalDate;
import java.util.List;

@Repository
public interface BorrowingRecordRepository extends JpaRepository<BorrowingRecord, Long> {

    // Найти все записи для конкретного читателя
    List<BorrowingRecord> findByPatronId(Long patronId);

    // Найти все записи для конкретной книги
    List<BorrowingRecord> findByBookId(Long bookId);

    // Найти текущие (не возвращенные) книги читателя
    List<BorrowingRecord> findByPatronIdAndReturnedFalse(Long patronId);

    // Проверить, взята ли книга (не возвращена)
    boolean existsByBookIdAndReturnedFalse(Long bookId);

    // Найти просроченные книги (текущая дата > dueDate и не возвращены)
    List<BorrowingRecord> findByReturnedFalseAndDueDateBefore(LocalDate date);

    // Свой SQL-запрос через JPQL [citation:8]
    @Query("SELECT br FROM BorrowingRecord br WHERE br.patron.id = :patronId AND br.returned = false")
    List<BorrowingRecord> findActiveBorrowingsByPatron(@Param("patronId") Long patronId);
}