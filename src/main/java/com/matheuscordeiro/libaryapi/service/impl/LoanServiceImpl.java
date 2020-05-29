package com.matheuscordeiro.libaryapi.service.impl;

import com.matheuscordeiro.libaryapi.exception.BusinessException;
import com.matheuscordeiro.libaryapi.model.entity.Loan;
import com.matheuscordeiro.libaryapi.model.repository.LoanRepository;
import com.matheuscordeiro.libaryapi.service.LoanService;

public class LoanServiceImpl implements LoanService {
    private LoanRepository repository;

    public LoanServiceImpl(LoanRepository repository) {
        this.repository = repository;
    }

    @Override
    public Loan save(Loan loan) {
        if(repository.existsByBookAndNotReturned(loan.getBook()))
            throw new BusinessException("Book already loaned");
        return repository.save(loan);
    }
}