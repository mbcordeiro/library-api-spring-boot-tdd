package com.matheuscordeiro.libaryapi.service;

import com.matheuscordeiro.libaryapi.exception.BusinessException;
import com.matheuscordeiro.libaryapi.model.entity.Book;
import com.matheuscordeiro.libaryapi.model.entity.Loan;
import com.matheuscordeiro.libaryapi.model.repository.LoanRepository;
import com.matheuscordeiro.libaryapi.service.impl.LoanServiceImpl;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.time.LocalDate;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.catchThrowable;
import static org.mockito.Mockito.*;

@ExtendWith(SpringExtension.class)
@ActiveProfiles("test")
public class LoanServiceTest {
    LoanService service;

    @MockBean
    LoanRepository repository;

    @BeforeEach
    public void setUp() {
        this.service = new LoanServiceImpl(repository);
    }

    @Test
    @DisplayName("Must save a loan")
    public void saveLoanTest() {
        Book book = Book.builder().id(1L).build();
        String customer = "Junior";
        Loan savingLoan = Loan.builder()
                .book(book)
                .customer(customer)
                .loanDate(LocalDate.now())
                .build();
        Loan savedLoan = Loan.builder()
                .id(1L)
                .book(book)
                .customer(customer)
                .loanDate(LocalDate.now())
                .build();
        when(repository.existsByBookAndNotReturned(book)).thenReturn(false);
        when(repository.save(savingLoan)).thenReturn(savedLoan);
        Loan loan = service.save(savingLoan);
        assertThat(loan.getId()).isEqualTo(savedLoan.getId());
        assertThat(loan.getBook()).isEqualTo(savedLoan.getBook().getId());
        assertThat(loan.getCustomer()).isEqualTo(savedLoan.getCustomer());
        assertThat(loan.getLoanDate()).isEqualTo(savedLoan.getLoanDate());
    }

    @Test
    @DisplayName("Must throw a business error when saving a loan with book already borrowed")
    public void loanedBookSaveTest() {
        Book book = Book.builder().id(1L).build();
        String customer = "Junior";
        Loan savingLoan = Loan.builder()
                .book(book)
                .customer(customer)
                .loanDate(LocalDate.now())
                .build();
        when(repository.existsByBookAndNotReturned(book)).thenReturn(true);
        Throwable exception = catchThrowable(() -> service.save(savingLoan));
        assertThat(exception)
                .isInstanceOf(BusinessException.class)
                .hasMessage("Book already loaned");
        verify(repository, never()).save(savingLoan);
    }
}