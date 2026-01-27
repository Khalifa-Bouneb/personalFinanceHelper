package com.finance.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.DBRef;
import org.springframework.data.mongodb.core.mapping.Document;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import java.math.BigDecimal;
import java.util.UUID;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Document(collection = "items")
public class Item {
    
    @Id
    private String id = UUID.randomUUID().toString();
    
    @NotBlank(message = "Item name is required")
    private String name;
    
    @NotNull(message = "Cost is required")
    private BigDecimal cost;
    
    @DBRef
    private Category category;
    
    private String categoryId;
}
