package com.sakshi.expensetracker.dto;

import java.util.List;

public class DashboardResponse {

    private DashboardSummaryResponse summary;

    private List<CategoryAnalyticsResponse> categoryBreakdown;

    private List<MonthlyTrendResponse> monthlyTrends;

    private List<ExpenseResponse> recentTransactions;

    public DashboardResponse(
            DashboardSummaryResponse summary,
            List<CategoryAnalyticsResponse> categoryBreakdown,
            List<MonthlyTrendResponse> monthlyTrends,
            List<ExpenseResponse> recentTransactions) {

        this.summary = summary;
        this.categoryBreakdown = categoryBreakdown;
        this.monthlyTrends = monthlyTrends;
        this.recentTransactions = recentTransactions;
    }

    public DashboardSummaryResponse getSummary() {
        return summary;
    }

    public List<CategoryAnalyticsResponse> getCategoryBreakdown() {
        return categoryBreakdown;
    }

    public List<MonthlyTrendResponse> getMonthlyTrends() {
        return monthlyTrends;
    }

    public List<ExpenseResponse> getRecentTransactions() {
        return recentTransactions;
    }
}