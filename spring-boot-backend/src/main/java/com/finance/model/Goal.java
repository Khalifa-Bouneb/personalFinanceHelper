package com.finance.model;

import com.finance.enums.GoalType;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.DBRef;
import org.springframework.data.mongodb.core.mapping.Document;

import jakarta.validation.constraints.NotNull;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.UUID;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Document(collection = "goals")
public class Goal {
    
    @Id
    private String id = UUID.randomUUID().toString();
    
    @NotNull(message = "User ID is required")
    private Integer userId;
    
    @DBRef
    private User user;
    
    private String categoryId;
    
    @DBRef
    private Category category;
    
    @NotNull(message = "Max amount is required")
    private BigDecimal maxAmount;
    
    @NotNull(message = "Goal type is required")
    private GoalType type;
    
    private LocalDate startDate;
    
    private LocalDate endDate;
    
    private LocalDateTime createdAt = LocalDateTime.now();
}
