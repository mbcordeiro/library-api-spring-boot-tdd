package com.matheuscordeiro.libaryapi.service;

import com.matheuscordeiro.libaryapi.model.entity.Book;
import com.matheuscordeiro.libaryapi.model.repository.BookRepository;
import com.matheuscordeiro.libaryapi.service.impl.BookServiceImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mockito;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import static org.assertj.core.api.Assertions.assertThat;

@ExtendWith(SpringExtension.class)
@ActiveProfiles("test")
public class BookServiceTest {
    BookService service;

    @MockBean
    BookRepository repository;

    @BeforeEach
    public void setUp() {
        this.service = new BookServiceImpl(repository);
    }

    @Test
    @DisplayName("Must save a book")
    public void  saveBookTest() {
        Book book = Book.builder().author("Junior").title("Futere").isbn("001").build();
        Mockito.when(repository.save(book))
                .thenReturn(
                        Book.builder()
                                .id(1L)
                                .title("Future")
                                .author("Junior")
                                .isbn("001")
                                .build()
                );
        Book savedBook = service.save(book);
        assertThat(savedBook.getId()).isNotNull();
        assertThat(savedBook.getTitle()).isEqualTo("Future");
        assertThat(savedBook.getAuthor()).isEqualTo("Junior");
        assertThat(savedBook.getIsbn()).isEqualTo("001");
    }
}