package com.example.demo.service;

import com.example.demo.model.Patron;
import com.example.demo.repository.PatronRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class PatronService {

    @Autowired
    private PatronRepository patronRepository;

    public List<Patron> getAllPatrons() {
        return patronRepository.findAll();
    }

    public Patron getPatronById(Long id) {
        return patronRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Читатель с ID " + id + " не найден"));
    }

    @Transactional
    public Patron addPatron(Patron patron) {
        if (patron.getEmail() != null && !patron.getEmail().isEmpty()) {
            patronRepository.findByEmail(patron.getEmail())
                    .ifPresent(p -> {
                        throw new RuntimeException("Читатель с email " + patron.getEmail() + " уже существует");
                    });
        }
        return patronRepository.save(patron);
    }

    @Transactional
    public Patron updatePatron(Long id, Patron patronDetails) {
        Patron patron = getPatronById(id);

        patron.setName(patronDetails.getName());
        patron.setEmail(patronDetails.getEmail());
        patron.setPhone(patronDetails.getPhone());

        return patronRepository.save(patron);
    }

    @Transactional
    public void deletePatron(Long id) {
        Patron patron = getPatronById(id);
        if (!patron.getBorrowingRecords().isEmpty()) {
            throw new RuntimeException("Нельзя удалить читателя с историей выдач");
        }
        patronRepository.delete(patron);
    }

    public List<Patron> searchPatronsByName(String name) {
        return patronRepository.findByNameContainingIgnoreCase(name);
    }
}