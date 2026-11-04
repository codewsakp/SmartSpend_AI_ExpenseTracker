package com.sakshi.expensetracker.ai.dto;

import java.math.BigDecimal;
import java.time.LocalDate;

public record ExpenseParseResponse(
        BigDecimal amount,
        String category,
        String description,
        LocalDate expenseDate
) {
}