
package com.sakshi.expensetracker.service;

import com.sakshi.expensetracker.dto.BudgetRequest;
import com.sakshi.expensetracker.dto.BudgetResponse;
import com.sakshi.expensetracker.dto.BudgetSummaryResponse;
import com.sakshi.expensetracker.entity.Budget;
import com.sakshi.expensetracker.entity.User;
import com.sakshi.expensetracker.repository.BudgetRepository;
import com.sakshi.expensetracker.repository.ExpenseRepository;
import com.sakshi.expensetracker.repository.UserRepository;

import jakarta.persistence.EntityNotFoundException;

import org.springframework.security.core.context.SecurityContextHolder;
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
    private final UserRepository userRepository;

    public BudgetService(
            BudgetRepository budgetRepository,
            ExpenseRepository expenseRepository,
            UserRepository userRepository) {

        this.budgetRepository = budgetRepository;
        this.expenseRepository = expenseRepository;
        this.userRepository = userRepository;
    }

    // Get the currently authenticated user
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

    // 1. Set or update a monthly budget
    public BudgetResponse setBudget(BudgetRequest request) {

        User user = getCurrentUser();

        Budget budget = budgetRepository
                .findByUserIdAndYearAndMonth(
                        user.getId(),
                        request.getYear(),
                        request.getMonth()
                )
                .orElseGet(Budget::new);

        budget.setAmount(request.getAmount());
        budget.setYear(request.getYear());
        budget.setMonth(request.getMonth());
        budget.setUser(user);

        Budget savedBudget = budgetRepository.save(budget);

        return convertToBudgetResponse(savedBudget);
    }

    // 2. Get monthly budget summary
    @Transactional(readOnly = true)
    public BudgetSummaryResponse getBudgetSummary(
            int year,
            int month) {

        User user = getCurrentUser();

        Budget budget = budgetRepository
                .findByUserIdAndYearAndMonth(
                        user.getId(),
                        year,
                        month
                )
                .orElseThrow(() ->
                        new EntityNotFoundException(
                                "Budget not found for " + year + "-" + month
                        )
                );

        YearMonth selectedMonth = YearMonth.of(year, month);

        LocalDate startDate = selectedMonth.atDay(1);
        LocalDate endDate = selectedMonth.plusMonths(1).atDay(1);

        BigDecimal totalSpent =
                expenseRepository.calculateMonthlyExpenses(
                        user.getId(),
                        startDate,
                        endDate
                );

        BigDecimal remainingAmount =
                budget.getAmount().subtract(totalSpent);

        BigDecimal percentageUsed =
                totalSpent
                        .multiply(BigDecimal.valueOf(100))
                        .divide(
                                budget.getAmount(),
                                2,
                                RoundingMode.HALF_UP
                        );

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

    // Convert Budget entity into Response DTO
    private BudgetResponse convertToBudgetResponse(Budget budget) {

        return new BudgetResponse(
                budget.getId(),
                budget.getAmount(),
                budget.getYear(),
                budget.getMonth()
        );
    }
}
