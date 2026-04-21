package com.example.BookStore.Service;

import com.example.BookStore.Repository.Author;
import com.example.BookStore.Repository.AuthorRepo;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@AllArgsConstructor
public class AuthorService {

    private final AuthorRepo authorRepository;

    public List<Author> getallAuthors() {
        return authorRepository.findAll();

    }
    public Author getAuthorbyid(int id){
        var auth=authorRepository.findById(id).orElse(null);
        if (auth==null){
         throw new IllegalArgumentException("No author exist of id"+id+"try again");

        }
        return  auth;

    }
    public Author save(Author author){
        return authorRepository.save(author);
    }

    public Author update(Author author){
        int id=author.getAuthor_id();
        var auth=authorRepository.findById(id).orElse(null);
        if (auth==null){
            throw new IllegalArgumentException("No author exist of id"+id+"try again");

        }
        auth.setName(author.getName());
        auth.setAuthor_id(author.getAuthor_id());
        auth.setRating(author.getAuthor_id());
        return authorRepository.save(auth);
    }
    public void delete(Author author){
        int id=author.getAuthor_id();
        var auth=authorRepository.findById(id).orElse(null);
        if (auth==null){
            throw new IllegalArgumentException("No author exist of id"+id+"try again");

        }
         authorRepository.delete(auth);
    }




}
