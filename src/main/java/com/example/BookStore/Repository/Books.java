package com.example.BookStore.Repository;


import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Component
@Scope("prototype")
public class Books {
    @Id
    private int book_id;
    private String name;
    @ManyToOne
    @JoinColumn(name = "author_id")
    private Author author;
    private double price;
    private String genre;
    private String Summary;


}


