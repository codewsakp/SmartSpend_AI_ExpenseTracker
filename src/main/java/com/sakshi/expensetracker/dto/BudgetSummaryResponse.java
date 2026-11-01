
package com.sakshi.expensetracker.dto;

import java.math.BigDecimal;

public class BudgetSummaryResponse {

    private BigDecimal budgetAmount;
    private BigDecimal totalSpent;
    private BigDecimal remainingAmount;
    private BigDecimal percentageUsed;
    private String status;

    public BudgetSummaryResponse(
            BigDecimal budgetAmount,
            BigDecimal totalSpent,
            BigDecimal remainingAmount,
            BigDecimal percentageUsed,
            String status) {

        this.budgetAmount = budgetAmount;
        this.totalSpent = totalSpent;
        this.remainingAmount = remainingAmount;
        this.percentageUsed = percentageUsed;
        this.status = status;
    }

    public BigDecimal getBudgetAmount() {
        return budgetAmount;
    }

    public BigDecimal getTotalSpent() {
        return totalSpent;
    }

    public BigDecimal getRemainingAmount() {
        return remainingAmount;
    }

    public BigDecimal getPercentageUsed() {
        return percentageUsed;
    }

    public String getStatus() {
        return status;
    }
}
