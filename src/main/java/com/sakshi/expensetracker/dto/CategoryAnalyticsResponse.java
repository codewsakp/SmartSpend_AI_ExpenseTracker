package com.sakshi.expensetracker.dto;

import java.math.BigDecimal;

public class CategoryAnalyticsResponse {

    private String category;
    private BigDecimal totalAmount;
    private BigDecimal percentage;

    public CategoryAnalyticsResponse(
            String category,
            BigDecimal totalAmount,
            BigDecimal percentage) {

        this.category = category;
        this.totalAmount = totalAmount;
        this.percentage = percentage;
    }

    public String getCategory() {
        return category;
    }

    public BigDecimal getTotalAmount() {
        return totalAmount;
    }

    public BigDecimal getPercentage() {
        return percentage;
    }
}