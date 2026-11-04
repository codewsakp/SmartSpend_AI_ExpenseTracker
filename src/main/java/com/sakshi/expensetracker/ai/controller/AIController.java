package com.sakshi.expensetracker.ai.controller;

import com.sakshi.expensetracker.ai.dto.ExpenseParseRequest;
import com.sakshi.expensetracker.ai.dto.ExpenseParseResponse;
import com.sakshi.expensetracker.ai.service.AIService;

import jakarta.validation.Valid;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/ai")
public class AIController {

    private final AIService aiService;

    public AIController(AIService aiService) {
        this.aiService = aiService;
    }

    @PostMapping("/parse-expense")
    public ResponseEntity<ExpenseParseResponse> parseExpense(
            @Valid @RequestBody ExpenseParseRequest request) {

        ExpenseParseResponse response =
                aiService.parseExpense(request.text());

        return ResponseEntity.ok(response);
    }
}