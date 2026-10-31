
package com.sakshi.expensetracker.service;

import com.sakshi.expensetracker.dto.ExpenseRequest;
import com.sakshi.expensetracker.dto.ExpenseResponse;
import com.sakshi.expensetracker.entity.Expense;
import com.sakshi.expensetracker.repository.ExpenseRepository;

import jakarta.persistence.EntityNotFoundException;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Transactional
public class ExpenseService {

    private final ExpenseRepository expenseRepository;

    // Constructor injection
    public ExpenseService(ExpenseRepository expenseRepository) {
        this.expenseRepository = expenseRepository;
    }

    // 1. Add a new expense
    public ExpenseResponse addExpense(ExpenseRequest request) {

        Expense expense = new Expense();

        expense.setAmount(request.getAmount());
        expense.setCategory(request.getCategory());
        expense.setDescription(request.getDescription());
        expense.setExpenseDate(request.getExpenseDate());
        expense.setPaymentMethod(request.getPaymentMethod());

        Expense savedExpense = expenseRepository.save(expense);

        return convertToResponse(savedExpense);
    }

    // 2. Get all expenses
    @Transactional(readOnly = true)
    public List<ExpenseResponse> getAllExpenses() {

        return expenseRepository.findAll()
                .stream()
                .map(this::convertToResponse)
                .toList();
    }

    // 3. Get expense by ID
    @Transactional(readOnly = true)
    public ExpenseResponse getExpenseById(Long id) {

        Expense expense = expenseRepository.findById(id)
                .orElseThrow(() ->
                        new EntityNotFoundException(
                                "Expense not found with ID: " + id
                        )
                );

        return convertToResponse(expense);
    }

    // 4. Update an existing expense
    public ExpenseResponse updateExpense(
            Long id,
            ExpenseRequest request) {

        Expense expense = expenseRepository.findById(id)
                .orElseThrow(() ->
                        new EntityNotFoundException(
                                "Expense not found with ID: " + id
                        )
                );

        expense.setAmount(request.getAmount());
        expense.setCategory(request.getCategory());
        expense.setDescription(request.getDescription());
        expense.setExpenseDate(request.getExpenseDate());
        expense.setPaymentMethod(request.getPaymentMethod());

        Expense updatedExpense = expenseRepository.save(expense);

        return convertToResponse(updatedExpense);
    }

    // 5. Delete an expense
    public void deleteExpense(Long id) {

        Expense expense = expenseRepository.findById(id)
                .orElseThrow(() ->
                        new EntityNotFoundException(
                                "Expense not found with ID: " + id
                        )
                );

        expenseRepository.delete(expense);
    }

    // Convert Entity into Response DTO
    private ExpenseResponse convertToResponse(Expense expense) {

        return new ExpenseResponse(
                expense.getId(),
                expense.getAmount(),
                expense.getCategory(),
                expense.getDescription(),
                expense.getExpenseDate(),
                expense.getPaymentMethod()
        );
    }
}
