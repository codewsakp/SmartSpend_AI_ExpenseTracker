package com.sakshi.expensetracker.service;

import com.sakshi.expensetracker.dto.BudgetSummaryResponse;
import com.sakshi.expensetracker.dto.DashboardSummaryResponse;

import com.sakshi.expensetracker.repository.ExpenseRepository;
import com.sakshi.expensetracker.dto.CategoryAnalyticsResponse;

import org.springframework.stereotype.Service;
import com.sakshi.expensetracker.entity.Expense;
import com.sakshi.expensetracker.dto.ExpenseResponse;

import org.springframework.data.domain.PageRequest;
import java.util.stream.Collectors;

import com.sakshi.expensetracker.dto.MonthlyTrendResponse;

import java.time.YearMonth;
import java.time.format.TextStyle;
import java.util.Locale;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.LocalDate;
import java.time.YearMonth;
import java.util.ArrayList;
import java.util.List;

@Service
public class DashboardService {

    private final BudgetService budgetService;
    private final ExpenseRepository expenseRepository;
    public DashboardService(BudgetService budgetService,ExpenseRepository expenseRepository) {
        this.budgetService = budgetService;
        this.expenseRepository = expenseRepository;
    }

    public DashboardSummaryResponse getDashboardSummary(
            int year, int month) {

        // Reuse the existing BudgetService calculation
        BudgetSummaryResponse budgetSummary =
                budgetService.getBudgetSummary(year, month);

        return new DashboardSummaryResponse(
                budgetSummary.getTotalSpent(),
                budgetSummary.getBudgetAmount(),
                budgetSummary.getRemainingAmount(),
                budgetSummary.getPercentageUsed(),
                budgetSummary.getStatus()
        );
    }
    public List<CategoryAnalyticsResponse> getCategoryBreakdown(
            int year, int month) {

        // Find the beginning and ending dates of the month
        YearMonth yearMonth = YearMonth.of(year, month);

        LocalDate startDate = yearMonth.atDay(1);
        LocalDate endDate = yearMonth.plusMonths(1).atDay(1);

        // Fetch category-wise totals from the database
        List<Object[]> results =
                expenseRepository.findCategoryWiseExpenses(
                        startDate, endDate);

        // Calculate total monthly spending
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
                        .divide(totalExpenses, 2, RoundingMode.HALF_UP);
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
    public List<MonthlyTrendResponse> getMonthlyTrends(
            int year, int month) {

        // Validate and create the selected month
        YearMonth selectedMonth = YearMonth.of(year, month);

        List<MonthlyTrendResponse> response = new ArrayList<>();

        // Calculate the last six months, including selected month
        for (int i = 5; i >= 0; i--) {

            YearMonth currentMonth = selectedMonth.minusMonths(i);

            LocalDate startDate = currentMonth.atDay(1);
            LocalDate endDate = currentMonth.plusMonths(1).atDay(1);

            // Calculate expenses for this month
            BigDecimal totalAmount =
                    expenseRepository.calculateMonthlyExpenses(
                            startDate, endDate);

            String monthName = currentMonth.getMonth()
                    .getDisplayName(
                            TextStyle.FULL,
                            Locale.ENGLISH
                    );

            // Add the monthly result
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
    public List<ExpenseResponse> getRecentTransactions() {

        // Retrieve the latest 5 expenses
        List<Expense> expenses =
                expenseRepository.findAllByOrderByExpenseDateDescIdDesc(
                        PageRequest.of(0, 5)
                );

        // Convert Expense entities into response DTOs
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
}