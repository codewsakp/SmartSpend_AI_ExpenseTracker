
package com.sakshi.expensetracker.service;

import com.sakshi.expensetracker.dto.ExpenseRequest;
import com.sakshi.expensetracker.dto.ExpenseResponse;
import com.sakshi.expensetracker.entity.Expense;
import com.sakshi.expensetracker.entity.User;
import com.sakshi.expensetracker.repository.ExpenseRepository;
import com.sakshi.expensetracker.repository.UserRepository;

import jakarta.persistence.EntityNotFoundException;

import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Transactional
public class ExpenseService {

    private final ExpenseRepository expenseRepository;
    private final UserRepository userRepository;

    public ExpenseService(
            ExpenseRepository expenseRepository,
            UserRepository userRepository) {
        this.expenseRepository = expenseRepository;
        this.userRepository = userRepository;
    }

    // Get the currently authenticated user from the JWT security context
    private User getCurrentUser() {
        String email = SecurityContextHolder
                .getContext()
                .getAuthentication()
                .getName();

        return userRepository.findByEmail(email)
                .orElseThrow(() ->
                        new EntityNotFoundException("User not found")
                );
    }

    // 1. Add a new expense
    public ExpenseResponse addExpense(ExpenseRequest request) {

        User user = getCurrentUser();

        Expense expense = new Expense();
        expense.setAmount(request.getAmount());
        expense.setCategory(request.getCategory());
        expense.setDescription(request.getDescription());
        expense.setExpenseDate(request.getExpenseDate());
        expense.setPaymentMethod(request.getPaymentMethod());
        expense.setUser(user);

        Expense savedExpense = expenseRepository.save(expense);

        return convertToResponse(savedExpense);
    }

    // 2. Get all expenses belonging to the logged-in user
    @Transactional(readOnly = true)
    public List<ExpenseResponse> getAllExpenses() {

        User user = getCurrentUser();

        return expenseRepository
                .findAllByUserIdOrderByExpenseDateDescIdDesc(user.getId())
                .stream()
                .map(this::convertToResponse)
                .toList();
    }

    // 3. Get an expense by ID, only if it belongs to this user
    @Transactional(readOnly = true)
    public ExpenseResponse getExpenseById(Long id) {

        User user = getCurrentUser();

        Expense expense = expenseRepository
                .findByIdAndUserId(id, user.getId())
                .orElseThrow(() ->
                        new EntityNotFoundException("Expense not found")
                );

        return convertToResponse(expense);
    }

    // 4. Update an expense belonging to this user
    public ExpenseResponse updateExpense(
            Long id,
            ExpenseRequest request) {

        User user = getCurrentUser();

        Expense expense = expenseRepository
                .findByIdAndUserId(id, user.getId())
                .orElseThrow(() ->
                        new EntityNotFoundException("Expense not found")
                );

        expense.setAmount(request.getAmount());
        expense.setCategory(request.getCategory());
        expense.setDescription(request.getDescription());
        expense.setExpenseDate(request.getExpenseDate());
        expense.setPaymentMethod(request.getPaymentMethod());

        Expense updatedExpense = expenseRepository.save(expense);

        return convertToResponse(updatedExpense);
    }

    // 5. Delete an expense belonging to this user
    public void deleteExpense(Long id) {

        User user = getCurrentUser();

        Expense expense = expenseRepository
                .findByIdAndUserId(id, user.getId())
                .orElseThrow(() ->
                        new EntityNotFoundException("Expense not found")
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
