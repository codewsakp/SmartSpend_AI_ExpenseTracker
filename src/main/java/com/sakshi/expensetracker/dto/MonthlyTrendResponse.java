package com.sakshi.expensetracker.dto;

import java.math.BigDecimal;

public class MonthlyTrendResponse {

    private String month;
    private int year;
    private BigDecimal totalAmount;

    public MonthlyTrendResponse(
            String month,
            int year,
            BigDecimal totalAmount) {

        this.month = month;
        this.year = year;
        this.totalAmount = totalAmount;
    }

    public String getMonth() {
        return month;
    }

    public int getYear() {
        return year;
    }

    public BigDecimal getTotalAmount() {
        return totalAmount;
    }
}