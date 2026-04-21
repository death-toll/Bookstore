package com.example.BookStore.Controller;

import com.example.BookStore.Repository.Author;
import com.example.BookStore.Service.AuthorService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
public class AuthorController {
    @Autowired
    private final AuthorService authorService;

    public AuthorController(AuthorService authorService) {
        this.authorService = authorService;
    }
    @GetMapping("/get/authors")
    public List<Author> authors(){
        return authorService.getallAuthors();

    }
    @PostMapping("/post/author")
    public Author author(@RequestBody Author author){
        return authorService.save(author);

    }
    @PatchMapping("/update/author")


}
