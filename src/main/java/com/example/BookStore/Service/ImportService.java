package com.example.BookStore.Service;

import com.example.BookStore.Repository.Author;
import com.example.BookStore.Repository.AuthorRepo;
import com.example.BookStore.Repository.BookRepo;
import com.example.BookStore.Repository.Books;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;

@Service
public class ImportService {

    private final AuthorRepo authorRepo;
    private final BookRepo bookRepo;

    public ImportService(AuthorRepo authorRepo, BookRepo bookRepo) {
        this.authorRepo = authorRepo;
        this.bookRepo = bookRepo;
    }

    @Transactional
    public int upsertAuthorsCsv(MultipartFile file) throws Exception {
        List<Author> authors = parseAuthors(file);
        authorRepo.saveAll(authors);    // upsert by @Id (author_id)
        return authors.size();
    }

    @Transactional
    public int upsertBooksCsv(MultipartFile file) throws Exception {
        List<Books> books = parseBooks(file);
        bookRepo.saveAll(books);        // upsert by @Id (book_id)
        return books.size();
    }

    private List<Author> parseAuthors(MultipartFile file) throws Exception {
        // Expected CSV header: author_id,name,rating
        if (file == null || file.isEmpty()) {
            throw new IllegalArgumentException("authors CSV file is empty");
        }

        List<Author> authors = new ArrayList<>();
        try (BufferedReader br = new BufferedReader(new InputStreamReader(file.getInputStream(), StandardCharsets.UTF_8))) {
            String line;
            boolean first = true;
            while ((line = br.readLine()) != null) {
                line = line.trim();
                if (line.isEmpty()) continue;
                if (first) { // header
                    first = false;
                    continue;
                }

                String[] parts = splitCsvLine(line, 3);
                int id = Integer.parseInt(parts[0].trim());
                String name = unquote(parts[1].trim());
                int rating = Integer.parseInt(parts[2].trim());

                Author a = new Author();
                a.setAuthor_id(id);
                a.setName(name);
                a.setRating(rating);
                authors.add(a);
            }
        }

        return authors;
    }

    private List<Books> parseBooks(MultipartFile file) throws Exception {
        // Expected CSV header: book_id,name,author_id,price,genre,summary
        if (file == null || file.isEmpty()) {
            throw new IllegalArgumentException("books CSV file is empty");
        }

        List<Books> books = new ArrayList<>();
        try (BufferedReader br = new BufferedReader(new InputStreamReader(file.getInputStream(), StandardCharsets.UTF_8))) {
            String line;
            boolean first = true;
            while ((line = br.readLine()) != null) {
                line = line.trim();
                if (line.isEmpty()) continue;
                if (first) { // header
                    first = false;
                    continue;
                }

                String[] parts = splitCsvLine(line, 6);

                int bookId = Integer.parseInt(parts[0].trim());
                String name = unquote(parts[1].trim());
                int authorId = Integer.parseInt(parts[2].trim());
                double price = Double.parseDouble(parts[3].trim());
                String genre = unquote(parts[4].trim());
                String summary = unquote(parts[5].trim());

                Author author = authorRepo.findById(authorId)
                        .orElseThrow(() -> new IllegalArgumentException("Author with id " + authorId + " not found for book_id=" + bookId));

                Books b = new Books();
                b.setBook_id(bookId);
                b.setName(name);
                b.setAuthor(author);
                b.setPrice(price);
                b.setGenre(genre);
                b.setSummary(summary);
                books.add(b);
            }
        }

        return books;
    }

    /**
     * Minimal CSV splitter that supports quotes around fields.
     */
    private static String[] splitCsvLine(String line, int expectedColumns) {
        String[] out = new String[expectedColumns];
        StringBuilder current = new StringBuilder();
        int col = 0;
        boolean inQuotes = false;

        for (int i = 0; i < line.length(); i++) {
            char c = line.charAt(i);
            if (c == '"') {
                inQuotes = !inQuotes;
                continue;
            }
            if (c == ',' && !inQuotes) {
                if (col >= expectedColumns) break;
                out[col++] = current.toString();
                current.setLength(0);
            } else {
                current.append(c);
            }
        }
        if (col < expectedColumns) {
            out[col++] = current.toString();
        }
        if (col != expectedColumns) {
            throw new IllegalArgumentException(
                    "Invalid CSV line, expected " + expectedColumns + " columns but got " + col + ": " + line);
        }
        return out;
    }

    private static String unquote(String s) {
        String t = s.trim();
        if (t.length() >= 2 && t.startsWith("\"") && t.endsWith("\"")) {
            return t.substring(1, t.length() - 1);
        }
        return t;
    }
}
