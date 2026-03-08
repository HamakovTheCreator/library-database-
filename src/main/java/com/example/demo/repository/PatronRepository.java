package com.example.demo.repository;

import com.example.demo.model.Patron;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.Optional;
import java.util.List;

@Repository
public interface PatronRepository extends JpaRepository<Patron, Long> {
    // Найти читателя по email
    Optional<Patron> findByEmail(String email);

    // Найти по имени (частичное совпадение)
    List<Patron> findByNameContainingIgnoreCase(String name);
}