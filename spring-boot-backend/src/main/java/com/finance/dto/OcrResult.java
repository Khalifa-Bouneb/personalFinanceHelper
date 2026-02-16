package com.finance.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class OcrResult {
    private BigDecimal amount;
    private String category;
    private String itemName;
    private String description;
    private LocalDateTime date;
    private String currency;
    private String rawText;
    private double confidence;
}
