package com.sakshi.expensetracker.controller;

import com.sakshi.expensetracker.dto.DashboardSummaryResponse;
import com.sakshi.expensetracker.service.DashboardService;
import com.sakshi.expensetracker.dto.CategoryAnalyticsResponse;
import java.util.List;
import org.springframework.web.bind.annotation.*;
import com.sakshi.expensetracker.dto.MonthlyTrendResponse;
import com.sakshi.expensetracker.dto.ExpenseResponse;
import com.sakshi.expensetracker.dto.DashboardResponse;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.Max;
import org.springframework.validation.annotation.Validated;

@RestController
@RequestMapping("/api/dashboard")
@Validated
public class DashboardController {

    private final DashboardService dashboardService;

    public DashboardController(DashboardService dashboardService) {
        this.dashboardService = dashboardService;
    }

    @GetMapping("/summary")
    public DashboardSummaryResponse getDashboardSummary(
            @RequestParam
            @Min(2020) @Max(2100) int year,

            @RequestParam
            @Min(1) @Max(12) int month) {

        return dashboardService.getDashboardSummary(year, month);
    }
    @GetMapping("/category-breakdown")
    public List<CategoryAnalyticsResponse> getCategoryBreakdown(
            @RequestParam
            @Min(2020) @Max(2100) int year,

            @RequestParam
            @Min(1) @Max(12) int month) {

        return dashboardService.getCategoryBreakdown(year, month);
    }
    @GetMapping("/monthly-trends")
    public List<MonthlyTrendResponse> getMonthlyTrends(
            @RequestParam
            @Min(2020) @Max(2100) int year,

            @RequestParam
            @Min(1) @Max(12) int month) {

        return dashboardService.getMonthlyTrends(year, month);
    }
    @GetMapping("/recent-transactions")
    public List<ExpenseResponse> getRecentTransactions() {

        return dashboardService.getRecentTransactions();
    }
    @GetMapping
    public DashboardResponse getDashboard(
            @RequestParam
            @Min(2020) @Max(2100) int year,

            @RequestParam
            @Min(1) @Max(12) int month) {

        return dashboardService.getDashboardData(year, month);
    }
}