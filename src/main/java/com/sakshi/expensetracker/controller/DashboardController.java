package com.sakshi.expensetracker.controller;

import com.sakshi.expensetracker.dto.DashboardSummaryResponse;
import com.sakshi.expensetracker.service.DashboardService;
import com.sakshi.expensetracker.dto.CategoryAnalyticsResponse;
import java.util.List;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/dashboard")
public class DashboardController {

    private final DashboardService dashboardService;

    public DashboardController(DashboardService dashboardService) {
        this.dashboardService = dashboardService;
    }

    @GetMapping("/summary")
    public DashboardSummaryResponse getDashboardSummary(
            @RequestParam int year,
            @RequestParam int month) {

        return dashboardService.getDashboardSummary(year, month);
    }
    @GetMapping("/category-breakdown")
    public List<CategoryAnalyticsResponse> getCategoryBreakdown(
            @RequestParam int year,
            @RequestParam int month) {

        return dashboardService.getCategoryBreakdown(year, month);
    }
}