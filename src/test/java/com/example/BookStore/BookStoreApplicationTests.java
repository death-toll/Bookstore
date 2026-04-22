package com.example.BookStore;

import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

import static org.assertj.core.api.Assertions.assertThat;

import org.springframework.beans.factory.annotation.Autowired;

import com.example.BookStore.Controller.AuthorController;
import com.example.BookStore.Controller.BookController;
import com.example.BookStore.Repository.AuthorRepo;
import com.example.BookStore.Repository.BookRepo;
import com.example.BookStore.Service.AuthorService;
import com.example.BookStore.Service.BookService;

@SpringBootTest
class BookStoreApplicationTests {
	@Autowired(required = false)
	private BookController bookController;

	@Autowired(required = false)
	private AuthorController authorController;

	@Autowired(required = false)
	private BookService bookService;

	@Autowired(required = false)
	private AuthorService authorService;

	@Autowired(required = false)
	private BookRepo bookRepo;

	@Autowired(required = false)
	private AuthorRepo authorRepo;

	@Test
	void contextLoads() {
		// If the application context fails to start, this test fails automatically.
		assertThat(true).isTrue();
	}

	@Test
	void bookControllerBeanIsCreated() {
		assertThat(bookController).isNotNull();
	}

	@Test
	void authorControllerBeanIsCreated() {
		assertThat(authorController).isNotNull();
	}

	@Test
	void serviceBeansAreCreated() {
		assertThat(bookService).isNotNull();
		assertThat(authorService).isNotNull();
	}

	@Test
	void repositoryBeansAreCreated() {
		assertThat(bookRepo).isNotNull();
		assertThat(authorRepo).isNotNull();
	}

}
