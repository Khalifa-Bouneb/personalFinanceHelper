package com.finance.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.util.List;
import java.util.Map;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class DashboardStats {
    private BigDecimal totalIncome;
    private BigDecimal totalExpenses;
    private BigDecimal balance;
    private BigDecimal monthlyIncome;
    private BigDecimal monthlyExpenses;
    private int totalTransactions;
    private List<CategoryBreakdown> categoryBreakdown;
    private List<MonthlyTrend> monthlyTrends;
    private List<GoalProgress> goalProgress;

    @Data
    @AllArgsConstructor
    @NoArgsConstructor
    public static class CategoryBreakdown {
        private String categoryId;
        private String categoryName;
        private String color;
        private BigDecimal total;
        private double percentage;
    }

    @Data
    @AllArgsConstructor
    @NoArgsConstructor
    public static class MonthlyTrend {
        private String month;
        private BigDecimal income;
        private BigDecimal expenses;
    }

    @Data
    @AllArgsConstructor
    @NoArgsConstructor
    public static class GoalProgress {
        private String goalId;
        private String categoryName;
        private String type;
        private BigDecimal maxAmount;
        private BigDecimal currentSpent;
        private double percentage;
        private boolean exceeded;
    }
}
