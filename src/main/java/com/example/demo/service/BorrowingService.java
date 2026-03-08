package com.example.demo.service;

import com.example.demo.model.Book;
import com.example.demo.model.Patron;
import com.example.demo.model.BorrowingRecord;
import com.example.demo.repository.BorrowingRecordRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.List;

@Service
public class BorrowingService {

    @Autowired
    private BorrowingRecordRepository borrowingRecordRepository;

    @Autowired
    private BookService bookService;

    @Autowired
    private PatronService patronService;

    @Transactional
    public BorrowingRecord borrowBook(Long bookId, Long patronId) {
        // Получаем книгу и читателя
        Book book = bookService.getBookById(bookId);
        Patron patron = patronService.getPatronById(patronId);

        // Проверяем, доступна ли книга
        if (!book.isAvailable()) {
            throw new RuntimeException("Книга \"" + book.getTitle() + "\" сейчас недоступна");
        }

        // Проверяем, не взята ли уже эта книга
        if (borrowingRecordRepository.existsByBookIdAndReturnedFalse(bookId)) {
            throw new RuntimeException("Эта книга уже выдана другому читателю");
        }

        // Помечаем книгу как недоступную
        book.setAvailable(false);
        bookService.updateBook(bookId, book); // Сохраняем изменение статуса книги

        // Создаем запись о выдаче
        BorrowingRecord record = new BorrowingRecord();
        record.setBook(book);
        record.setPatron(patron);
        record.setBorrowDate(LocalDate.now());
        record.setDueDate(LocalDate.now().plusDays(14));
        record.setReturned(false);

        return borrowingRecordRepository.save(record);
    }

    @Transactional
    public BorrowingRecord returnBook(Long bookId, Long patronId) {
        // Находим активные записи читателя
        List<BorrowingRecord> activeRecords = borrowingRecordRepository
                .findByPatronIdAndReturnedFalse(patronId);

        BorrowingRecord record = activeRecords.stream()
                .filter(r -> r.getBook().getId().equals(bookId))
                .findFirst()
                .orElseThrow(() -> new RuntimeException("Запись о выдаче этой книги читателю не найдена"));

        // Отмечаем книгу как доступную
        Book book = record.getBook();
        book.setAvailable(true);
        bookService.updateBook(bookId, book); // Сохраняем изменение статуса книги

        // Обновляем запись
        record.setReturned(true);
        record.setReturnDate(LocalDate.now());

        return borrowingRecordRepository.save(record);
    }

    // Получить историю выдач читателя
    public List<BorrowingRecord> getPatronBorrowingHistory(Long patronId) {
        patronService.getPatronById(patronId); // проверяем, что читатель существует
        return borrowingRecordRepository.findByPatronId(patronId);
    }

    // Получить историю выдач книги
    public List<BorrowingRecord> getBookBorrowingHistory(Long bookId) {
        bookService.getBookById(bookId); // проверяем, что книга существует
        return borrowingRecordRepository.findByBookId(bookId);
    }

    // Получить текущие выдачи читателя
    public List<BorrowingRecord> getPatronCurrentBorrowings(Long patronId) {
        patronService.getPatronById(patronId);
        return borrowingRecordRepository.findByPatronIdAndReturnedFalse(patronId);
    }

    // Получить просроченные книги
    public List<BorrowingRecord> getOverdueBooks() {
        return borrowingRecordRepository.findByReturnedFalseAndDueDateBefore(LocalDate.now());
    }
}