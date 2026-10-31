
package com.sakshi.expensetracker.controller;

import com.sakshi.expensetracker.dto.ExpenseRequest;
import com.sakshi.expensetracker.dto.ExpenseResponse;
import com.sakshi.expensetracker.service.ExpenseService;

import jakarta.validation.Valid;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/expenses")
public class ExpenseController {

    private final ExpenseService expenseService;

    // Constructor injection
    public ExpenseController(ExpenseService expenseService) {
        this.expenseService = expenseService;
    }

    // 1. Add a new expense
    @PostMapping
    public ResponseEntity<ExpenseResponse> addExpense(
            @Valid @RequestBody ExpenseRequest request) {

        ExpenseResponse response =
                expenseService.addExpense(request);

        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(response);
    }

    // 2. Get all expenses
    @GetMapping
    public ResponseEntity<List<ExpenseResponse>> getAllExpenses() {

        List<ExpenseResponse> expenses =
                expenseService.getAllExpenses();

        return ResponseEntity.ok(expenses);
    }

    // 3. Get expense by ID
    @GetMapping("/{id}")
    public ResponseEntity<ExpenseResponse> getExpenseById(
            @PathVariable Long id) {

        ExpenseResponse response =
                expenseService.getExpenseById(id);

        return ResponseEntity.ok(response);
    }

    // 4. Update an expense
    @PutMapping("/{id}")
    public ResponseEntity<ExpenseResponse> updateExpense(
            @PathVariable Long id,
            @Valid @RequestBody ExpenseRequest request) {

        ExpenseResponse response =
                expenseService.updateExpense(id, request);

        return ResponseEntity.ok(response);
    }

    // 5. Delete an expense
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteExpense(
            @PathVariable Long id) {

        expenseService.deleteExpense(id);

        return ResponseEntity.noContent().build();
    }
}
