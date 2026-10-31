
package com.sakshi.expensetracker.dto;

import java.math.BigDecimal;

public class BudgetResponse {

    private Long id;
    private BigDecimal amount;
    private int year;
    private int month;

    public BudgetResponse(
            Long id,
            BigDecimal amount,
            int year,
            int month) {

        this.id = id;
        this.amount = amount;
        this.year = year;
        this.month = month;
    }

    public Long getId() {
        return id;
    }

    public BigDecimal getAmount() {
        return amount;
    }

    public int getYear() {
        return year;
    }

    public int getMonth() {
        return month;
    }
}
