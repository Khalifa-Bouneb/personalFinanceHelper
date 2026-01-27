package com.finance.model;

import com.finance.enums.TransactionSign;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.DBRef;
import org.springframework.data.mongodb.core.mapping.Document;

import jakarta.validation.constraints.NotNull;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.UUID;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Document(collection = "transactions")
public class Transaction {
    
    @Id
    private String id = UUID.randomUUID().toString();
    
    @NotNull(message = "User ID is required")
    private Integer userId;
    
    @DBRef
    private User user;
    
    @NotNull(message = "Amount is required")
    private BigDecimal amount;
    
    private LocalDateTime transactionDate = LocalDateTime.now();
    
    @NotNull(message = "Transaction sign is required")
    private TransactionSign sign;
    
    private String categoryId;
    
    @DBRef
    private Category category;
    
    private String itemId;
    
    @DBRef
    private Item item;
}
