package com.example.BookStore.Controller;

import com.example.BookStore.Service.ImportService;

import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;


import java.util.Map;

@RestController
@RequestMapping("/import")
public class Import {

    private final ImportService importService;

    public Import(ImportService importService) {
        this.importService = importService;
    }

    @PostMapping(value = "/authors", consumes = "multipart/form-data")
    public Map<String, Object> importAuthors(@RequestParam("file") MultipartFile file) throws Exception {
        int count = importService.upsertAuthorsCsv(file);
        return Map.of("entity", "authors", "upserted", count);
    }

    @PostMapping(value = "/books", consumes = "multipart/form-data")
    public Map<String, Object> importBooks(@RequestParam("file") MultipartFile file) throws Exception {
        int count = importService.upsertBooksCsv(file);
        return Map.of("entity", "books", "upserted", count);
    }
}
