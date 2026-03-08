package com.example.demo.controller;

import com.example.demo.model.BorrowingRecord;
import com.example.demo.service.BorrowingService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api")
public class BorrowingController {

    @Autowired
    private BorrowingService borrowingService;

    // POST /api/borrow/{bookId}/patron/{patronId} - выдать книгу
    @PostMapping("/borrow/{bookId}/patron/{patronId}")
    @ResponseStatus(HttpStatus.CREATED)
    public BorrowingRecord borrowBook(
            @PathVariable Long bookId,
            @PathVariable Long patronId) {
        return borrowingService.borrowBook(bookId, patronId);
    }

    // PUT /api/return/{bookId}/patron/{patronId} - вернуть книгу
    @PutMapping("/return/{bookId}/patron/{patronId}")
    public BorrowingRecord returnBook(
            @PathVariable Long bookId,
            @PathVariable Long patronId) {
        return borrowingService.returnBook(bookId, patronId);
    }

    // GET /api/patrons/{patronId}/borrowings - история читателя
    @GetMapping("/patrons/{patronId}/borrowings")
    public List<BorrowingRecord> getPatronBorrowingHistory(@PathVariable Long patronId) {
        return borrowingService.getPatronBorrowingHistory(patronId);
    }

    // GET /api/books/{bookId}/borrowings - история книги
    @GetMapping("/books/{bookId}/borrowings")
    public List<BorrowingRecord> getBookBorrowingHistory(@PathVariable Long bookId) {
        return borrowingService.getBookBorrowingHistory(bookId);
    }

    // GET /api/patrons/{patronId}/borrowings/current - текущие книги читателя
    @GetMapping("/patrons/{patronId}/borrowings/current")
    public List<BorrowingRecord> getPatronCurrentBorrowings(@PathVariable Long patronId) {
        return borrowingService.getPatronCurrentBorrowings(patronId);
    }

    // GET /api/borrowings/overdue - просроченные книги
    @GetMapping("/borrowings/overdue")
    public List<BorrowingRecord> getOverdueBooks() {
        return borrowingService.getOverdueBooks();
    }
}