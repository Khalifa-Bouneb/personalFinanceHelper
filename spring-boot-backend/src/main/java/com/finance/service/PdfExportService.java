package com.finance.service;

import com.finance.dto.DashboardStats;
import com.finance.enums.TransactionSign;
import com.finance.model.Transaction;
import com.finance.model.User;
import com.finance.repository.TransactionRepository;
import com.finance.repository.UserRepository;
import com.itextpdf.kernel.colors.ColorConstants;
import com.itextpdf.kernel.colors.DeviceRgb;
import com.itextpdf.kernel.pdf.PdfDocument;
import com.itextpdf.kernel.pdf.PdfWriter;
import com.itextpdf.layout.Document;
import com.itextpdf.layout.element.Cell;
import com.itextpdf.layout.element.Paragraph;
import com.itextpdf.layout.element.Table;
import com.itextpdf.layout.properties.TextAlignment;
import com.itextpdf.layout.properties.UnitValue;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.io.ByteArrayOutputStream;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;

@Service
@RequiredArgsConstructor
@Slf4j
public class PdfExportService {

    private final TransactionRepository transactionRepository;
    private final UserRepository userRepository;
    private final AnalyticsService analyticsService;

    /**
     * Generate a certified PDF financial report for a user.
     */
    public byte[] generateReport(Integer userId) {
        try {
            ByteArrayOutputStream baos = new ByteArrayOutputStream();
            PdfWriter writer = new PdfWriter(baos);
            PdfDocument pdfDoc = new PdfDocument(writer);
            Document document = new Document(pdfDoc);

            User user = userRepository.findById(userId).orElse(null);
            DashboardStats stats = analyticsService.getDashboardStats(userId);
            List<Transaction> transactions = transactionRepository.findByUserId(userId);

            // Header
            DeviceRgb headerColor = new DeviceRgb(102, 126, 234);
            Paragraph title = new Paragraph("SMART FINANCE MANAGER")
                    .setFontSize(24)
                    .setBold()
                    .setFontColor(headerColor)
                    .setTextAlignment(TextAlignment.CENTER);
            document.add(title);

            Paragraph subtitle = new Paragraph("Financial Report")
                    .setFontSize(16)
                    .setFontColor(ColorConstants.GRAY)
                    .setTextAlignment(TextAlignment.CENTER);
            document.add(subtitle);

            document.add(new Paragraph("\n"));

            // User info
            if (user != null) {
                document.add(new Paragraph("Prepared for: " + user.getName())
                        .setFontSize(12).setBold());
                document.add(new Paragraph("Email: " + user.getEmail())
                        .setFontSize(10));
                document.add(new Paragraph("Currency: " + (user.getCurrency() != null ? user.getCurrency() : "N/A"))
                        .setFontSize(10));
                document.add(new Paragraph("Generated: " +
                        LocalDateTime.now().format(DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm")))
                        .setFontSize(10));
            }

            document.add(new Paragraph("\n"));

            // Summary
            document.add(new Paragraph("Financial Summary")
                    .setFontSize(16).setBold().setFontColor(headerColor));

            Table summaryTable = new Table(UnitValue.createPercentArray(new float[]{1, 1}))
                    .useAllAvailableWidth();

            addSummaryRow(summaryTable, "Total Income", stats.getTotalIncome().toString());
            addSummaryRow(summaryTable, "Total Expenses", stats.getTotalExpenses().toString());
            addSummaryRow(summaryTable, "Balance", stats.getBalance().toString());
            addSummaryRow(summaryTable, "Monthly Income", stats.getMonthlyIncome().toString());
            addSummaryRow(summaryTable, "Monthly Expenses", stats.getMonthlyExpenses().toString());
            addSummaryRow(summaryTable, "Total Transactions",
                    String.valueOf(stats.getTotalTransactions()));

            document.add(summaryTable);
            document.add(new Paragraph("\n"));

            // Category Breakdown
            if (stats.getCategoryBreakdown() != null && !stats.getCategoryBreakdown().isEmpty()) {
                document.add(new Paragraph("Expense Breakdown by Category")
                        .setFontSize(14).setBold().setFontColor(headerColor));

                Table catTable = new Table(UnitValue.createPercentArray(new float[]{2, 1, 1}))
                        .useAllAvailableWidth();

                catTable.addHeaderCell(new Cell().add(new Paragraph("Category").setBold())
                        .setBackgroundColor(headerColor).setFontColor(ColorConstants.WHITE));
                catTable.addHeaderCell(new Cell().add(new Paragraph("Total").setBold())
                        .setBackgroundColor(headerColor).setFontColor(ColorConstants.WHITE));
                catTable.addHeaderCell(new Cell().add(new Paragraph("Percentage").setBold())
                        .setBackgroundColor(headerColor).setFontColor(ColorConstants.WHITE));

                for (DashboardStats.CategoryBreakdown cb : stats.getCategoryBreakdown()) {
                    catTable.addCell(new Cell().add(new Paragraph(cb.getCategoryName())));
                    catTable.addCell(new Cell().add(new Paragraph(cb.getTotal().toString())));
                    catTable.addCell(new Cell().add(
                            new Paragraph(String.format("%.1f%%", cb.getPercentage()))));
                }

                document.add(catTable);
                document.add(new Paragraph("\n"));
            }

            // Transaction list
            document.add(new Paragraph("Transaction History")
                    .setFontSize(14).setBold().setFontColor(headerColor));

            Table txTable = new Table(UnitValue.createPercentArray(new float[]{2, 2, 1.5f, 1}))
                    .useAllAvailableWidth();

            txTable.addHeaderCell(new Cell().add(new Paragraph("Date").setBold())
                    .setBackgroundColor(headerColor).setFontColor(ColorConstants.WHITE));
            txTable.addHeaderCell(new Cell().add(new Paragraph("Category").setBold())
                    .setBackgroundColor(headerColor).setFontColor(ColorConstants.WHITE));
            txTable.addHeaderCell(new Cell().add(new Paragraph("Amount").setBold())
                    .setBackgroundColor(headerColor).setFontColor(ColorConstants.WHITE));
            txTable.addHeaderCell(new Cell().add(new Paragraph("Type").setBold())
                    .setBackgroundColor(headerColor).setFontColor(ColorConstants.WHITE));

            for (Transaction t : transactions) {
                String date = t.getTransactionDate() != null
                        ? t.getTransactionDate().format(DateTimeFormatter.ofPattern("dd/MM/yyyy"))
                        : "N/A";
                String category = t.getCategory() != null ? t.getCategory().getName() : "N/A";
                String amount = (t.getSign() == TransactionSign.POSITIVE ? "+" : "-") +
                        t.getAmount().toString();

                txTable.addCell(new Cell().add(new Paragraph(date)));
                txTable.addCell(new Cell().add(new Paragraph(category)));
                txTable.addCell(new Cell().add(new Paragraph(amount)
                        .setFontColor(t.getSign() == TransactionSign.POSITIVE
                                ? new DeviceRgb(39, 174, 96)
                                : new DeviceRgb(231, 76, 60))));
                txTable.addCell(new Cell().add(new Paragraph(t.getSign().name())));
            }

            document.add(txTable);
            document.add(new Paragraph("\n"));

            // Goal progress
            if (stats.getGoalProgress() != null && !stats.getGoalProgress().isEmpty()) {
                document.add(new Paragraph("Goal Progress")
                        .setFontSize(14).setBold().setFontColor(headerColor));

                Table goalTable = new Table(UnitValue.createPercentArray(new float[]{2, 1, 1, 1, 1}))
                        .useAllAvailableWidth();

                goalTable.addHeaderCell(new Cell().add(new Paragraph("Category").setBold())
                        .setBackgroundColor(headerColor).setFontColor(ColorConstants.WHITE));
                goalTable.addHeaderCell(new Cell().add(new Paragraph("Type").setBold())
                        .setBackgroundColor(headerColor).setFontColor(ColorConstants.WHITE));
                goalTable.addHeaderCell(new Cell().add(new Paragraph("Max").setBold())
                        .setBackgroundColor(headerColor).setFontColor(ColorConstants.WHITE));
                goalTable.addHeaderCell(new Cell().add(new Paragraph("Spent").setBold())
                        .setBackgroundColor(headerColor).setFontColor(ColorConstants.WHITE));
                goalTable.addHeaderCell(new Cell().add(new Paragraph("Progress").setBold())
                        .setBackgroundColor(headerColor).setFontColor(ColorConstants.WHITE));

                for (DashboardStats.GoalProgress gp : stats.getGoalProgress()) {
                    goalTable.addCell(new Cell().add(new Paragraph(gp.getCategoryName())));
                    goalTable.addCell(new Cell().add(new Paragraph(gp.getType())));
                    goalTable.addCell(new Cell().add(new Paragraph(gp.getMaxAmount().toString())));
                    goalTable.addCell(new Cell().add(new Paragraph(gp.getCurrentSpent().toString())));
                    goalTable.addCell(new Cell().add(new Paragraph(
                            String.format("%.1f%%", gp.getPercentage()))
                            .setFontColor(gp.isExceeded()
                                    ? new DeviceRgb(231, 76, 60)
                                    : new DeviceRgb(39, 174, 96))));
                }

                document.add(goalTable);
            }

            // Footer
            document.add(new Paragraph("\n\n"));
            document.add(new Paragraph("This report was auto-generated by Smart Finance Manager")
                    .setFontSize(8)
                    .setFontColor(ColorConstants.GRAY)
                    .setTextAlignment(TextAlignment.CENTER));
            document.add(new Paragraph("Confidential - For personal use only")
                    .setFontSize(8)
                    .setFontColor(ColorConstants.GRAY)
                    .setTextAlignment(TextAlignment.CENTER));

            document.close();
            return baos.toByteArray();
        } catch (Exception e) {
            log.error("Error generating PDF report: {}", e.getMessage(), e);
            throw new RuntimeException("Failed to generate PDF report", e);
        }
    }

    private void addSummaryRow(Table table, String label, String value) {
        table.addCell(new Cell().add(new Paragraph(label).setBold())
                .setBackgroundColor(new DeviceRgb(248, 249, 250)));
        table.addCell(new Cell().add(new Paragraph(value)));
    }
}
