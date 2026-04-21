package com.example.BookStore.Repository;
import com.example.BookStore.Repository.Books;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface  BookRepo extends JpaRepository<Books, Integer> {
    Optional<Books> findByName(String name);
}

