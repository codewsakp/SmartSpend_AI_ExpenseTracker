
package com.sakshi.expensetracker.service;

import com.sakshi.expensetracker.dto.CategoryAnalyticsResponse;
import com.sakshi.expensetracker.dto.DashboardResponse;
import com.sakshi.expensetracker.dto.DashboardSummaryResponse;
import com.sakshi.expensetracker.dto.ExpenseResponse;
import com.sakshi.expensetracker.dto.MonthlyTrendResponse;

import com.sakshi.expensetracker.entity.Budget;
import com.sakshi.expensetracker.entity.Expense;

import com.sakshi.expensetracker.repository.BudgetRepository;
import com.sakshi.expensetracker.repository.ExpenseRepository;

import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.LocalDate;
import java.time.YearMonth;
import java.time.format.TextStyle;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.stream.Collectors;

@Service
public class DashboardService {

    private final BudgetRepository budgetRepository;
    private final ExpenseRepository expenseRepository;

    public DashboardService(
            BudgetRepository budgetRepository,
            ExpenseRepository expenseRepository) {

        this.budgetRepository = budgetRepository;
        this.expenseRepository = expenseRepository;
    }

    // 1. Get monthly dashboard summary
    public DashboardSummaryResponse getDashboardSummary(
            int year, int month) {

        YearMonth yearMonth = YearMonth.of(year, month);

        LocalDate startDate = yearMonth.atDay(1);
        LocalDate endDate = yearMonth.plusMonths(1).atDay(1);

        // Calculate total expenses for the selected month
        BigDecimal totalExpenses =
                expenseRepository.calculateMonthlyExpenses(
                        startDate, endDate);

        // Check whether a budget exists for this month
        Budget budget = budgetRepository
                .findByYearAndMonth(year, month)
                .orElse(null);

        // If no budget exists, still return the expense summary
        if (budget == null) {

            return new DashboardSummaryResponse(
                    totalExpenses,
                    null,
                    null,
                    null,
                    "BUDGET_NOT_SET"
            );
        }

        // Calculate budget details
        BigDecimal budgetAmount = budget.getAmount();

        BigDecimal remainingBudget =
                budgetAmount.subtract(totalExpenses);

        BigDecimal percentageUsed = totalExpenses
                .multiply(BigDecimal.valueOf(100))
                .divide(budgetAmount, 2, RoundingMode.HALF_UP);

        String budgetStatus =
                remainingBudget.compareTo(BigDecimal.ZERO) < 0
                        ? "OVER_BUDGET"
                        : "WITHIN_BUDGET";

        return new DashboardSummaryResponse(
                totalExpenses,
                budgetAmount,
                remainingBudget,
                percentageUsed,
                budgetStatus
        );
    }

    // 2. Get category-wise expense breakdown
    public List<CategoryAnalyticsResponse> getCategoryBreakdown(
            int year, int month) {

        YearMonth yearMonth = YearMonth.of(year, month);

        LocalDate startDate = yearMonth.atDay(1);
        LocalDate endDate = yearMonth.plusMonths(1).atDay(1);

        List<Object[]> results =
                expenseRepository.findCategoryWiseExpenses(
                        startDate, endDate);

        // Calculate total monthly expenses
        BigDecimal totalExpenses = results.stream()
                .map(row -> (BigDecimal) row[1])
                .reduce(BigDecimal.ZERO, BigDecimal::add);

        List<CategoryAnalyticsResponse> response =
                new ArrayList<>();

        // Convert database results into DTO objects
        for (Object[] row : results) {

            String category = (String) row[0];
            BigDecimal amount = (BigDecimal) row[1];

            BigDecimal percentage = BigDecimal.ZERO;

            if (totalExpenses.compareTo(BigDecimal.ZERO) > 0) {

                percentage = amount
                        .multiply(BigDecimal.valueOf(100))
                        .divide(
                                totalExpenses,
                                2,
                                RoundingMode.HALF_UP
                        );
            }

            response.add(
                    new CategoryAnalyticsResponse(
                            category,
                            amount,
                            percentage
                    )
            );
        }

        return response;
    }

    // 3. Get spending trends for the last six months
    public List<MonthlyTrendResponse> getMonthlyTrends(
            int year, int month) {

        YearMonth selectedMonth = YearMonth.of(year, month);

        List<MonthlyTrendResponse> response =
                new ArrayList<>();

        // Include the selected month and previous five months
        for (int i = 5; i >= 0; i--) {

            YearMonth currentMonth =
                    selectedMonth.minusMonths(i);

            LocalDate startDate = currentMonth.atDay(1);
            LocalDate endDate =
                    currentMonth.plusMonths(1).atDay(1);

            BigDecimal totalAmount =
                    expenseRepository.calculateMonthlyExpenses(
                            startDate, endDate);

            String monthName = currentMonth.getMonth()
                    .getDisplayName(
                            TextStyle.FULL,
                            Locale.ENGLISH
                    );

            response.add(
                    new MonthlyTrendResponse(
                            monthName,
                            currentMonth.getYear(),
                            totalAmount
                    )
            );
        }

        return response;
    }

    // 4. Get the latest five transactions
    public List<ExpenseResponse> getRecentTransactions() {

        List<Expense> expenses =
                expenseRepository.findAllByOrderByExpenseDateDescIdDesc(
                        PageRequest.of(0, 5)
                );

        return expenses.stream()
                .map(expense -> new ExpenseResponse(
                        expense.getId(),
                        expense.getAmount(),
                        expense.getCategory(),
                        expense.getDescription(),
                        expense.getExpenseDate(),
                        expense.getPaymentMethod()
                ))
                .collect(Collectors.toList());
    }

    // 5. Combine all dashboard information
    public DashboardResponse getDashboardData(
            int year, int month) {

        DashboardSummaryResponse summary =
                getDashboardSummary(year, month);

        List<CategoryAnalyticsResponse> categoryBreakdown =
                getCategoryBreakdown(year, month);

        List<MonthlyTrendResponse> monthlyTrends =
                getMonthlyTrends(year, month);

        List<ExpenseResponse> recentTransactions =
                getRecentTransactions();

        return new DashboardResponse(
                summary,
                categoryBreakdown,
                monthlyTrends,
                recentTransactions
        );
    }
}
