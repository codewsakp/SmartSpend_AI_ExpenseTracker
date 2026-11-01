
package com.sakshi.expensetracker.service;

import com.sakshi.expensetracker.dto.BudgetRequest;
import com.sakshi.expensetracker.dto.BudgetResponse;
import com.sakshi.expensetracker.dto.BudgetSummaryResponse;

import com.sakshi.expensetracker.entity.Budget;

import com.sakshi.expensetracker.repository.BudgetRepository;
import com.sakshi.expensetracker.repository.ExpenseRepository;

import jakarta.persistence.EntityNotFoundException;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.LocalDate;
import java.time.YearMonth;

@Service
@Transactional
public class BudgetService {

    private final BudgetRepository budgetRepository;
    private final ExpenseRepository expenseRepository;

    // Constructor injection
    public BudgetService(
            BudgetRepository budgetRepository,
            ExpenseRepository expenseRepository) {

        this.budgetRepository = budgetRepository;
        this.expenseRepository = expenseRepository;
    }

    // 1. Set or update a monthly budget
    public BudgetResponse setBudget(BudgetRequest request) {

        // Find existing budget for the requested month
        Budget budget = budgetRepository
                .findByYearAndMonth(
                        request.getYear(),
                        request.getMonth()
                )
                .orElseGet(Budget::new);

        // Set budget details
        budget.setAmount(request.getAmount());
        budget.setYear(request.getYear());
        budget.setMonth(request.getMonth());

        // Save new or updated budget
        Budget savedBudget = budgetRepository.save(budget);

        return convertToBudgetResponse(savedBudget);
    }

    // 2. Get monthly budget summary
    @Transactional(readOnly = true)
    public BudgetSummaryResponse getBudgetSummary(
            int year,
            int month) {

        // Find the budget
        Budget budget = budgetRepository
                .findByYearAndMonth(year, month)
                .orElseThrow(() ->
                        new EntityNotFoundException(
                                "Budget not found for "
                                        + year + "-" + month
                        )
                );

        // Determine the date range
        YearMonth selectedMonth = YearMonth.of(year, month);

        LocalDate startDate =
                selectedMonth.atDay(1);

        LocalDate endDate =
                selectedMonth.plusMonths(1).atDay(1);

        // Calculate total expenses for that month
        BigDecimal totalSpent =
                expenseRepository.calculateMonthlyExpenses(
                        startDate,
                        endDate
                );

        // Calculate remaining budget
        BigDecimal remainingAmount =
                budget.getAmount().subtract(totalSpent);

        // Calculate percentage used
        BigDecimal percentageUsed =
                totalSpent
                        .multiply(BigDecimal.valueOf(100))
                        .divide(
                                budget.getAmount(),
                                2,
                                RoundingMode.HALF_UP
                        );

        // Determine budget status
        String status;

        if (remainingAmount.compareTo(BigDecimal.ZERO) < 0) {
            status = "OVER_BUDGET";
        } else {
            status = "WITHIN_BUDGET";
        }

        return new BudgetSummaryResponse(
                budget.getAmount(),
                totalSpent,
                remainingAmount,
                percentageUsed,
                status
        );
    }

    // Convert Budget entity into response DTO
    private BudgetResponse convertToBudgetResponse(
            Budget budget) {

        return new BudgetResponse(
                budget.getId(),
                budget.getAmount(),
                budget.getYear(),
                budget.getMonth()
        );
    }
}
