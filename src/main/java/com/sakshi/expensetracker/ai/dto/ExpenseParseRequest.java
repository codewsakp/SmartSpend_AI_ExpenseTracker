package com.sakshi.expensetracker.ai.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public record ExpenseParseRequest(

        @NotBlank(message = "Please enter an expense description")
        @Size(max = 500, message = "Expense description cannot exceed 500 characters")
        String text

) {
}