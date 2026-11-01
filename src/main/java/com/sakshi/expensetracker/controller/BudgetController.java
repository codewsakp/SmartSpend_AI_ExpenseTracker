
package com.sakshi.expensetracker.controller;

import com.sakshi.expensetracker.dto.BudgetRequest;
import com.sakshi.expensetracker.dto.BudgetResponse;
import com.sakshi.expensetracker.dto.BudgetSummaryResponse;

import com.sakshi.expensetracker.service.BudgetService;

import jakarta.validation.Valid;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/budgets")
public class BudgetController {

    private final BudgetService budgetService;

    // Constructor injection
    public BudgetController(BudgetService budgetService) {
        this.budgetService = budgetService;
    }

    // 1. Set or update monthly budget
    @PutMapping
    public ResponseEntity<BudgetResponse> setBudget(
            @Valid @RequestBody BudgetRequest request) {

        BudgetResponse response =
                budgetService.setBudget(request);

        return ResponseEntity
                .status(HttpStatus.OK)
                .body(response);
    }

    // 2. Get monthly budget summary
    @GetMapping("/summary")
    public ResponseEntity<BudgetSummaryResponse> getBudgetSummary(
            @RequestParam int year,
            @RequestParam int month) {

        BudgetSummaryResponse response =
                budgetService.getBudgetSummary(year, month);

        return ResponseEntity.ok(response);
    }
}
