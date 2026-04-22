package com.example.BookStore.repository;

import com.example.BookStore.Repository.Author;
import com.example.BookStore.Repository.AuthorRepo;
import com.example.BookStore.Repository.BookRepo;
import com.example.BookStore.Repository.Books;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;

import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;

@DataJpaTest
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
class BookRepoTest {

    @Autowired
    private BookRepo bookRepo;

    @Autowired
    private AuthorRepo authorRepo;

    @BeforeEach
    void setup() {
        bookRepo.deleteAll();
        authorRepo.deleteAll();

        Author a = new Author();
        a.setAuthor_id(1);
        a.setName("Author One");
        a.setRating(5);
        authorRepo.save(a);

        Books b = new Books();
        b.setBook_id(101);
        b.setName("Clean Code");
        b.setGenre("Programming");
        b.setPrice(20.0);
        b.setAuthor(a);
        b.setSummary("A book about writing cleaner code");
        bookRepo.save(b);
    }

    @Test
    void findByName_returnsBook_whenPresent() {
        Optional<Books> found = bookRepo.findByName("Clean Code");
        assertThat(found).isPresent();
        assertThat(found.get().getBook_id()).isEqualTo(101);
    }

    @Test
    void findByName_returnsEmpty_whenNotPresent() {
        assertThat(bookRepo.findByName("Unknown")).isEmpty();
    }

    @Test
    void existsById_returnsTrue_whenPresent() {
        assertThat(bookRepo.existsById(101)).isTrue();
        assertThat(bookRepo.existsById(999)).isFalse();
    }

    @Test
    void save_persistsBook() {
        Author a = authorRepo.findById(1).orElseThrow();
        Books b2 = new Books();
        b2.setBook_id(102);
        b2.setName("Effective Java");
        b2.setGenre("Programming");
        b2.setPrice(25.0);
        b2.setAuthor(a);
        b2.setSummary("Java best practices");

        bookRepo.save(b2);
        assertThat(bookRepo.findById(102)).isPresent();
    }

    @Test
    void save_updatesExistingBook_whenSameId() {
        Books existing = bookRepo.findById(101).orElseThrow();
        existing.setPrice(99.99);
        bookRepo.save(existing);

        Books reloaded = bookRepo.findById(101).orElseThrow();
        assertThat(reloaded.getPrice()).isEqualTo(99.99);
        assertThat(bookRepo.count()).isEqualTo(1);
    }

    @Test
    void delete_removesBook() {
        bookRepo.deleteById(101);
        assertThat(bookRepo.findById(101)).isEmpty();
    }

    @Test
    void findAll_returnsAllBooks() {
        List<Books> all = bookRepo.findAll();
        assertThat(all).hasSize(1);
        assertThat(all.get(0).getName()).isEqualTo("Clean Code");
    }
}

