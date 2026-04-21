package com.example.BookStore.Service;

import com.example.BookStore.Repository.Books;
import jakarta.transaction.Transactional;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import com.example.BookStore.Repository.BookRepo;
import com.example.BookStore.Repository.AuthorRepo;

import java.util.List;
import java.util.Objects;


@AllArgsConstructor
@Service
public class BookService {
    private final BookRepo bookRepository;
    private final AuthorRepo authorRepository;

    public List<Books> getAllBooks() {
        return bookRepository.findAll();
    }

    public List<Books> filterBooks(String name,
                                  String genre,
                                  Double minPrice,
                                  Double maxPrice,
                                  String authorName) {
        return bookRepository.findAll().stream()
                .filter(b -> name == null || (b.getName() != null && b.getName().toLowerCase().contains(name.toLowerCase())))
                .filter(b -> genre == null || (b.getGenre() != null && b.getGenre().equalsIgnoreCase(genre)))
                .filter(b -> minPrice == null || b.getPrice() >= minPrice)
                .filter(b -> maxPrice == null || b.getPrice() <= maxPrice)
                .filter(b -> authorName == null || (b.getAuthor() != null && Objects.equals(b.getAuthor().getName(), authorName)))
                .toList();
    }

    public Books getBookById(Integer id) {
        return bookRepository.findById(id).orElse(new Books());

    }
    @Transactional
    public Books createBook(Books book) {
        // Ensure the author exists before saving the book
        var authorId = 0;
        if (book.getAuthor() != null) {
            authorId = book.getAuthor().getAuthor_id();
            var author = authorRepository.findById(authorId).orElse(null);
            if (author == null) {
                throw new IllegalArgumentException("Author with ID " + authorId + " does not exist.");
            }
            book.setAuthor(author);
        }

        return bookRepository.save(book);
    }
    @Transactional
    public Books updateBook( Books updatedBook) {
        int id = updatedBook.getBook_id();
        var existingBook = bookRepository.findById(id).orElse(null);
        if (existingBook == null) {
            throw new IllegalArgumentException("Book with ID " + id + " does not exist.");
        }

        // Update fields
        existingBook.setName(updatedBook.getName());
        existingBook.setPrice(updatedBook.getPrice());
        existingBook.setGenre(updatedBook.getGenre());
        existingBook.setSummary(updatedBook.getSummary());

        // Update author if provided
        if (updatedBook.getAuthor() != null) {
            var authorId = updatedBook.getAuthor().getAuthor_id();
            var author = authorRepository.findById(authorId).orElse(null);
            if (author == null) {
                throw new IllegalArgumentException("Author with ID " + authorId + " does not exist.");
            }
            existingBook.setAuthor(author);
        }

        return bookRepository.save(existingBook);
    }

    @Transactional
    public void deleteBookByName(String name) {
        Books b1 = bookRepository.findByName(name)
                .orElseThrow(() -> new IllegalArgumentException("Book not found with name: " + name));
        bookRepository.delete(b1);
    }



}



