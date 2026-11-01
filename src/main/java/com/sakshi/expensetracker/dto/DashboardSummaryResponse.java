package com.sakshi.expensetracker.dto;

import java.math.BigDecimal;

public class DashboardSummaryResponse {

    private BigDecimal totalExpenses;
    private BigDecimal monthlyBudget;
    private BigDecimal remainingBudget;
    private BigDecimal percentageUsed;
    private String budgetStatus;

    public DashboardSummaryResponse(
            BigDecimal totalExpenses,
            BigDecimal monthlyBudget,
            BigDecimal remainingBudget,
            BigDecimal percentageUsed,
            String budgetStatus) {

        this.totalExpenses = totalExpenses;
        this.monthlyBudget = monthlyBudget;
        this.remainingBudget = remainingBudget;
        this.percentageUsed = percentageUsed;
        this.budgetStatus = budgetStatus;
    }

    public BigDecimal getTotalExpenses() {
        return totalExpenses;
    }

    public BigDecimal getMonthlyBudget() {
        return monthlyBudget;
    }

    public BigDecimal getRemainingBudget() {
        return remainingBudget;
    }

    public BigDecimal getPercentageUsed() {
        return percentageUsed;
    }

    public String getBudgetStatus() {
        return budgetStatus;
    }
}