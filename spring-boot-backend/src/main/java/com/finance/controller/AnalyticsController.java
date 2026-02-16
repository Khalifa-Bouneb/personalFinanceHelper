package com.finance.controller;

import com.finance.dto.DashboardStats;
import com.finance.dto.ForecastResult;
import com.finance.service.AnalyticsService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/analytics")
@RequiredArgsConstructor
@CrossOrigin(origins = "http://localhost:4200")
public class AnalyticsController {

    private final AnalyticsService analyticsService;

    /**
     * Get dashboard statistics for a user (totals, category breakdown, trends, goal progress)
     */
    @GetMapping("/stats/{userId}")
    public ResponseEntity<DashboardStats> getStats(@PathVariable Integer userId) {
        return ResponseEntity.ok(analyticsService.getDashboardStats(userId));
    }

    /**
     * Get AI-powered forecast: predicted end-of-month balance, daily predictions,
     * anomaly alerts, and AI budget recommendation.
     */
    @GetMapping("/forecast/{userId}")
    public ResponseEntity<ForecastResult> getForecast(@PathVariable Integer userId) {
        return ResponseEntity.ok(analyticsService.getForecast(userId));
    }
}
