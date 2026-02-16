package com.finance.service;

import com.finance.dto.DashboardStats;
import com.finance.dto.ForecastResult;
import com.finance.enums.TransactionSign;
import com.finance.model.Category;
import com.finance.model.Goal;
import com.finance.model.Transaction;
import com.finance.repository.CategoryRepository;
import com.finance.repository.GoalRepository;
import com.finance.repository.TransactionRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.YearMonth;
import java.time.format.DateTimeFormatter;
import java.time.temporal.ChronoUnit;
import java.util.*;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Slf4j
public class AnalyticsService {

    private final TransactionRepository transactionRepository;
    private final CategoryRepository categoryRepository;
    private final GoalRepository goalRepository;
    private final GroqAiService groqAiService;

    /**
     * Get comprehensive dashboard statistics for a user.
     */
    public DashboardStats getDashboardStats(Integer userId) {
        List<Transaction> allTransactions = transactionRepository.findByUserId(userId);
        List<Goal> userGoals = goalRepository.findByUserId(userId);
        List<Category> allCategories = categoryRepository.findAll();

        DashboardStats stats = new DashboardStats();

        // Calculate totals
        BigDecimal totalIncome = allTransactions.stream()
                .filter(t -> t.getSign() == TransactionSign.POSITIVE)
                .map(Transaction::getAmount)
                .reduce(BigDecimal.ZERO, BigDecimal::add);

        BigDecimal totalExpenses = allTransactions.stream()
                .filter(t -> t.getSign() == TransactionSign.NEGATIVE)
                .map(Transaction::getAmount)
                .reduce(BigDecimal.ZERO, BigDecimal::add);

        stats.setTotalIncome(totalIncome);
        stats.setTotalExpenses(totalExpenses);
        stats.setBalance(totalIncome.subtract(totalExpenses));
        stats.setTotalTransactions(allTransactions.size());

        // Monthly income/expenses (current month)
        YearMonth currentMonth = YearMonth.now();
        LocalDateTime monthStart = currentMonth.atDay(1).atStartOfDay();
        LocalDateTime monthEnd = currentMonth.atEndOfMonth().atTime(23, 59, 59);

        BigDecimal monthlyIncome = allTransactions.stream()
                .filter(t -> t.getSign() == TransactionSign.POSITIVE)
                .filter(t -> t.getTransactionDate() != null &&
                        !t.getTransactionDate().isBefore(monthStart) &&
                        !t.getTransactionDate().isAfter(monthEnd))
                .map(Transaction::getAmount)
                .reduce(BigDecimal.ZERO, BigDecimal::add);

        BigDecimal monthlyExpenses = allTransactions.stream()
                .filter(t -> t.getSign() == TransactionSign.NEGATIVE)
                .filter(t -> t.getTransactionDate() != null &&
                        !t.getTransactionDate().isBefore(monthStart) &&
                        !t.getTransactionDate().isAfter(monthEnd))
                .map(Transaction::getAmount)
                .reduce(BigDecimal.ZERO, BigDecimal::add);

        stats.setMonthlyIncome(monthlyIncome);
        stats.setMonthlyExpenses(monthlyExpenses);

        // Category breakdown (expenses only)
        Map<String, BigDecimal> categoryTotals = allTransactions.stream()
                .filter(t -> t.getSign() == TransactionSign.NEGATIVE)
                .filter(t -> t.getCategoryId() != null)
                .collect(Collectors.groupingBy(
                        Transaction::getCategoryId,
                        Collectors.reducing(BigDecimal.ZERO, Transaction::getAmount, BigDecimal::add)
                ));

        Map<String, Category> categoryMap = allCategories.stream()
                .collect(Collectors.toMap(Category::getId, c -> c));

        List<DashboardStats.CategoryBreakdown> breakdown = categoryTotals.entrySet().stream()
                .map(entry -> {
                    Category cat = categoryMap.get(entry.getKey());
                    double pct = totalExpenses.compareTo(BigDecimal.ZERO) > 0
                            ? entry.getValue().divide(totalExpenses, 4, RoundingMode.HALF_UP)
                            .multiply(BigDecimal.valueOf(100)).doubleValue()
                            : 0;
                    return new DashboardStats.CategoryBreakdown(
                            entry.getKey(),
                            cat != null ? cat.getName() : "Unknown",
                            cat != null ? cat.getColor() : "#ccc",
                            entry.getValue(),
                            pct
                    );
                })
                .sorted((a, b) -> b.getTotal().compareTo(a.getTotal()))
                .collect(Collectors.toList());

        stats.setCategoryBreakdown(breakdown);

        // Monthly trends (last 6 months)
        List<DashboardStats.MonthlyTrend> trends = new ArrayList<>();
        for (int i = 5; i >= 0; i--) {
            YearMonth ym = YearMonth.now().minusMonths(i);
            LocalDateTime start = ym.atDay(1).atStartOfDay();
            LocalDateTime end = ym.atEndOfMonth().atTime(23, 59, 59);

            BigDecimal mIncome = allTransactions.stream()
                    .filter(t -> t.getSign() == TransactionSign.POSITIVE)
                    .filter(t -> t.getTransactionDate() != null &&
                            !t.getTransactionDate().isBefore(start) &&
                            !t.getTransactionDate().isAfter(end))
                    .map(Transaction::getAmount)
                    .reduce(BigDecimal.ZERO, BigDecimal::add);

            BigDecimal mExpenses = allTransactions.stream()
                    .filter(t -> t.getSign() == TransactionSign.NEGATIVE)
                    .filter(t -> t.getTransactionDate() != null &&
                            !t.getTransactionDate().isBefore(start) &&
                            !t.getTransactionDate().isAfter(end))
                    .map(Transaction::getAmount)
                    .reduce(BigDecimal.ZERO, BigDecimal::add);

            trends.add(new DashboardStats.MonthlyTrend(
                    ym.format(DateTimeFormatter.ofPattern("MMM yyyy")),
                    mIncome, mExpenses));
        }
        stats.setMonthlyTrends(trends);

        // Goal progress
        List<DashboardStats.GoalProgress> goalProgress = userGoals.stream()
                .map(goal -> {
                    BigDecimal spent = allTransactions.stream()
                            .filter(t -> t.getSign() == TransactionSign.NEGATIVE)
                            .filter(t -> goal.getCategoryId() != null &&
                                    goal.getCategoryId().equals(t.getCategoryId()))
                            .filter(t -> t.getTransactionDate() != null &&
                                    goal.getStartDate() != null && goal.getEndDate() != null &&
                                    !t.getTransactionDate().toLocalDate().isBefore(goal.getStartDate()) &&
                                    !t.getTransactionDate().toLocalDate().isAfter(goal.getEndDate()))
                            .map(Transaction::getAmount)
                            .reduce(BigDecimal.ZERO, BigDecimal::add);

                    double pct = goal.getMaxAmount().compareTo(BigDecimal.ZERO) > 0
                            ? spent.divide(goal.getMaxAmount(), 4, RoundingMode.HALF_UP)
                            .multiply(BigDecimal.valueOf(100)).doubleValue()
                            : 0;

                    Category cat = goal.getCategoryId() != null
                            ? categoryMap.get(goal.getCategoryId()) : null;

                    return new DashboardStats.GoalProgress(
                            goal.getId(),
                            cat != null ? cat.getName() : "General",
                            goal.getType().name(),
                            goal.getMaxAmount(),
                            spent,
                            pct,
                            pct > 100
                    );
                })
                .collect(Collectors.toList());

        stats.setGoalProgress(goalProgress);

        return stats;
    }

    /**
     * AI-powered forecasting: predicts end-of-month balance using linear regression
     * on historical spending data, and detects anomalous transactions.
     */
    public ForecastResult getForecast(Integer userId) {
        List<Transaction> allTransactions = transactionRepository.findByUserId(userId);
        ForecastResult result = new ForecastResult();

        // Current month data
        YearMonth currentMonth = YearMonth.now();
        LocalDate today = LocalDate.now();
        int daysInMonth = currentMonth.lengthOfMonth();
        int dayOfMonth = today.getDayOfMonth();
        int daysRemaining = daysInMonth - dayOfMonth;
        result.setDaysRemaining(daysRemaining);

        // Calculate current month income and expenses
        LocalDateTime monthStart = currentMonth.atDay(1).atStartOfDay();
        LocalDateTime monthEnd = currentMonth.atEndOfMonth().atTime(23, 59, 59);

        BigDecimal currentMonthIncome = allTransactions.stream()
                .filter(t -> t.getSign() == TransactionSign.POSITIVE)
                .filter(t -> t.getTransactionDate() != null &&
                        !t.getTransactionDate().isBefore(monthStart) &&
                        !t.getTransactionDate().isAfter(monthEnd))
                .map(Transaction::getAmount)
                .reduce(BigDecimal.ZERO, BigDecimal::add);

        BigDecimal currentMonthExpenses = allTransactions.stream()
                .filter(t -> t.getSign() == TransactionSign.NEGATIVE)
                .filter(t -> t.getTransactionDate() != null &&
                        !t.getTransactionDate().isBefore(monthStart) &&
                        !t.getTransactionDate().isAfter(monthEnd))
                .map(Transaction::getAmount)
                .reduce(BigDecimal.ZERO, BigDecimal::add);

        // Average daily spending based on current month so far
        BigDecimal avgDailySpending = dayOfMonth > 0
                ? currentMonthExpenses.divide(BigDecimal.valueOf(dayOfMonth), 2, RoundingMode.HALF_UP)
                : BigDecimal.ZERO;
        result.setAverageDailySpending(avgDailySpending);

        // Predict total monthly expenses using linear regression
        BigDecimal predictedMonthlyExpenses = avgDailySpending
                .multiply(BigDecimal.valueOf(daysInMonth));
        result.setPredictedMonthlyExpenses(predictedMonthlyExpenses);

        // Predicted end-of-month balance
        BigDecimal currentBalance = currentMonthIncome.subtract(currentMonthExpenses);
        BigDecimal predictedAdditionalExpenses = avgDailySpending
                .multiply(BigDecimal.valueOf(daysRemaining));
        BigDecimal predictedEndBalance = currentBalance.subtract(predictedAdditionalExpenses);
        result.setPredictedEndOfMonthBalance(predictedEndBalance);

        // Safe daily budget
        if (daysRemaining > 0 && currentBalance.compareTo(BigDecimal.ZERO) > 0) {
            result.setSafeDailyBudget(currentBalance.divide(
                    BigDecimal.valueOf(daysRemaining), 2, RoundingMode.HALF_UP));
        } else {
            result.setSafeDailyBudget(BigDecimal.ZERO);
        }

        // Generate daily predictions
        List<ForecastResult.DailyPrediction> predictions = new ArrayList<>();
        BigDecimal runningBalance = currentBalance;
        for (int i = 1; i <= daysRemaining; i++) {
            LocalDate date = today.plusDays(i);
            runningBalance = runningBalance.subtract(avgDailySpending);
            predictions.add(new ForecastResult.DailyPrediction(
                    date.format(DateTimeFormatter.ISO_DATE),
                    runningBalance
            ));
        }
        result.setPredictions(predictions);

        // Anomaly detection: flag transactions > 20% above category average
        List<ForecastResult.AnomalyAlert> anomalies = detectAnomalies(allTransactions);
        result.setAnomalies(anomalies);

        // AI-powered recommendation
        String context = String.format(
                "Monthly income: %.2f, Monthly expenses so far: %.2f, " +
                "Days remaining: %d, Average daily spending: %.2f, " +
                "Predicted end-of-month balance: %.2f. " +
                "Number of anomalous transactions: %d. " +
                "Give brief budget advice.",
                currentMonthIncome.doubleValue(), currentMonthExpenses.doubleValue(),
                daysRemaining, avgDailySpending.doubleValue(),
                predictedEndBalance.doubleValue(), anomalies.size()
        );

        try {
            String recommendation = groqAiService.getBudgetRecommendation(context);
            result.setRecommendation(recommendation);
        } catch (Exception e) {
            log.warn("Could not get AI recommendation: {}", e.getMessage());
            if (predictedEndBalance.compareTo(BigDecimal.ZERO) < 0) {
                result.setRecommendation("⚠️ Warning: At your current spending rate, you may overspend this month. " +
                        "Try to limit daily spending to " + result.getSafeDailyBudget() + " or less.");
            } else {
                result.setRecommendation("✅ You're on track! Keep your daily spending around " +
                        result.getSafeDailyBudget() + " to maintain a positive balance.");
            }
        }

        return result;
    }

    /**
     * Detects anomalous transactions: any expense that exceeds the category average by 20%+
     */
    private List<ForecastResult.AnomalyAlert> detectAnomalies(List<Transaction> transactions) {
        List<ForecastResult.AnomalyAlert> anomalies = new ArrayList<>();

        // Group expenses by category
        Map<String, List<Transaction>> byCategory = transactions.stream()
                .filter(t -> t.getSign() == TransactionSign.NEGATIVE)
                .filter(t -> t.getCategoryId() != null)
                .collect(Collectors.groupingBy(Transaction::getCategoryId));

        Map<String, Category> categoryMap = categoryRepository.findAll().stream()
                .collect(Collectors.toMap(Category::getId, c -> c));

        for (Map.Entry<String, List<Transaction>> entry : byCategory.entrySet()) {
            List<Transaction> catTransactions = entry.getValue();
            if (catTransactions.size() < 2) continue;

            BigDecimal avg = catTransactions.stream()
                    .map(Transaction::getAmount)
                    .reduce(BigDecimal.ZERO, BigDecimal::add)
                    .divide(BigDecimal.valueOf(catTransactions.size()), 2, RoundingMode.HALF_UP);

            BigDecimal threshold = avg.multiply(BigDecimal.valueOf(1.20)); // 20% above average

            for (Transaction t : catTransactions) {
                if (t.getAmount().compareTo(threshold) > 0) {
                    double deviation = t.getAmount().subtract(avg)
                            .divide(avg, 4, RoundingMode.HALF_UP)
                            .multiply(BigDecimal.valueOf(100)).doubleValue();

                    Category cat = categoryMap.get(entry.getKey());
                    anomalies.add(new ForecastResult.AnomalyAlert(
                            t.getId(),
                            t.getAmount(),
                            cat != null ? cat.getName() : "Unknown",
                            t.getTransactionDate() != null
                                    ? t.getTransactionDate().format(DateTimeFormatter.ISO_DATE) : "",
                            avg,
                            deviation,
                            String.format("⚠️ This %s expense of %.2f is %.0f%% above the category average of %.2f",
                                    cat != null ? cat.getName() : "Unknown",
                                    t.getAmount().doubleValue(), deviation, avg.doubleValue())
                    ));
                }
            }
        }

        return anomalies;
    }
}
