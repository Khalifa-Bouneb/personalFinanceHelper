package com.finance.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ForecastResult {
    private BigDecimal predictedEndOfMonthBalance;
    private BigDecimal predictedMonthlyExpenses;
    private BigDecimal averageDailySpending;
    private int daysRemaining;
    private BigDecimal safeDailyBudget;
    private String recommendation;
    private List<DailyPrediction> predictions;
    private List<AnomalyAlert> anomalies;

    @Data
    @AllArgsConstructor
    @NoArgsConstructor
    public static class DailyPrediction {
        private String date;
        private BigDecimal predictedBalance;
    }

    @Data
    @AllArgsConstructor
    @NoArgsConstructor
    public static class AnomalyAlert {
        private String transactionId;
        private BigDecimal amount;
        private String categoryName;
        private String date;
        private BigDecimal categoryAverage;
        private double deviationPercentage;
        private String message;
    }
}
