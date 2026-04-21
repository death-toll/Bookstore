package com.example.BookStore.Repository;


import jakarta.persistence.Entity;
import jakarta.persistence.Id;
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
public class Author {
    @Id
    private int author_id;
    private String name;
    private int rating;

    @Override
    public String toString() {
        return "Author{" +
                "author_id=" + author_id +
                ", name='" + name + '\'' +
                ", rating=" + rating +
                '}';
    }
}
