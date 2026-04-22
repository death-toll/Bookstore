package com.example.BookStore.service;

import com.example.BookStore.Repository.Author;
import com.example.BookStore.Repository.AuthorRepo;
import com.example.BookStore.Repository.BookRepo;
import com.example.BookStore.Repository.Books;
import com.example.BookStore.Service.BookService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;

import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

class BookServiceTest {

    private BookRepo bookRepo;
    private AuthorRepo authorRepo;
    private BookService bookService;

    @BeforeEach
    void setUp() {
        bookRepo = mock(BookRepo.class);
        authorRepo = mock(AuthorRepo.class);
        bookService = new BookService(bookRepo, authorRepo);
    }

    @Test
    void getAllBooks_returnsRepositoryResults() {
        when(bookRepo.findAll()).thenReturn(List.of(new Books(), new Books()));
        assertThat(bookService.getAllBooks()).hasSize(2);
        verify(bookRepo).findAll();
    }

    @Test
    void filterBooks_filtersByName_caseInsensitiveContains() {
        Books b1 = new Books(1, "Clean Code", null, 20.0, "Programming", "...");
        Books b2 = new Books(2, "Harry Potter", null, 15.0, "Fiction", "...");
        when(bookRepo.findAll()).thenReturn(List.of(b1, b2));

        List<Books> out = bookService.filterBooks("clean", null, null, null, null);
        assertThat(out).extracting(Books::getName).containsExactly("Clean Code");
    }

    @Test
    void createBook_throwsWhenAuthorDoesNotExist() {
        Author missing = new Author();
        missing.setAuthor_id(99);

        Books book = new Books();
        book.setBook_id(10);
        book.setAuthor(missing);

        when(authorRepo.findById(99)).thenReturn(Optional.empty());

        assertThatThrownBy(() -> bookService.createBook(book))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessageContaining("Author with ID 99 does not exist");

        verify(bookRepo, never()).save(any());
    }

    @Test
    void createBook_setsManagedAuthorAndSaves() {
        Author detached = new Author();
        detached.setAuthor_id(1);

        Author managed = new Author();
        managed.setAuthor_id(1);
        managed.setName("Author One");

        Books book = new Books();
        book.setBook_id(101);
        book.setName("Clean Code");
        book.setAuthor(detached);

        when(authorRepo.findById(1)).thenReturn(Optional.of(managed));
        when(bookRepo.save(any(Books.class))).thenAnswer(inv -> inv.getArgument(0));

        Books saved = bookService.createBook(book);
        assertThat(saved.getAuthor()).isSameAs(managed);
        verify(bookRepo).save(book);
    }

    @Test
    void deleteBookByName_deletesWhenFound() {
        Books existing = new Books();
        existing.setBook_id(1);
        existing.setName("Clean Code");

        when(bookRepo.findByName("Clean Code")).thenReturn(Optional.of(existing));

        bookService.deleteBookByName("Clean Code");

        ArgumentCaptor<Books> captor = ArgumentCaptor.forClass(Books.class);
        verify(bookRepo).delete(captor.capture());
        assertThat(captor.getValue()).isSameAs(existing);
    }
}

