package com.example.BookStore.Controller;


import com.example.BookStore.Repository.Books;
import com.example.BookStore.Service.BookService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.security.web.csrf.CsrfToken;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.io.PrintWriter;
import java.nio.charset.StandardCharsets;
import java.util.List;

@RestController
public class BookController {
    private final BookService bookService;

    public BookController(BookService bookService) {
        this.bookService = bookService;
    }


    @GetMapping("/")
    public String home(){
        return "Welcome to the Book Store!";

    }
    @GetMapping("/get/books")
      public Object books(
          @RequestParam(required = false) String name,
          @RequestParam(required = false) String genre,
          @RequestParam(required = false, name = "min_price") Double minPrice,
          @RequestParam(required = false, name = "max_price") Double maxPrice,
          @RequestParam(required = false, name = "authorName") String authorName,
          @RequestParam(required = false, name = "csv_required", defaultValue = "no") String csvRequired,
          HttpServletResponse response
      ) throws IOException {

        List<Books> books = bookService.filterBooks(name, genre, minPrice, maxPrice, authorName);

        if (!"yes".equalsIgnoreCase(csvRequired)) {
          return books; // JSON response
        }

        // CSV download
        response.setCharacterEncoding(StandardCharsets.UTF_8.name());
        response.setContentType("text/csv");
        response.setHeader("Content-Disposition", "attachment; filename=books.csv");

        try (PrintWriter writer = response.getWriter()) {
          writer.println("book_id,name,author_id,author_name,price,genre,summary");
          for (Books b : books) {
            int aId = (b.getAuthor() != null) ? b.getAuthor().getAuthor_id() : 0;
            String aName = (b.getAuthor() != null) ? b.getAuthor().getName() : "";

            writer.printf("%d,%s,%d,%s,%.2f,%s,%s%n",
                b.getBook_id(),
                csvEscape(b.getName()),
                aId,
                csvEscape(aName),
                b.getPrice(),
                csvEscape(b.getGenre()),
                csvEscape(b.getSummary())
            );
          }
        }
        return null;
      }

      private static String csvEscape(String s) {
        if (s == null) return "";
        String t = s.replace("\"", "\"\"");
        if (t.contains(",") || t.contains("\n") || t.contains("\r")) {
          return "\"" + t + "\"";
        }
        return t;
      }
    @GetMapping("/csrf")
    public CsrfToken getcsrfToken(HttpServletRequest request){
        return (CsrfToken) request.getAttribute("_csrf");

    }
    @PostMapping("/demo")
    public String demo(){
        return "demo";
    }
    @PostMapping("/post/books")
    public Books books(@RequestBody Books b1){
        return bookService.createBook(b1);

    }
    @PutMapping("/update/book")
    public Books update(@RequestBody Books b1){
        return bookService.updateBook(b1);
    }
    @DeleteMapping("/delete/{book_name}")
    public void delete(@PathVariable String book_name){
        bookService.deleteBookByName(book_name);

    }

}
