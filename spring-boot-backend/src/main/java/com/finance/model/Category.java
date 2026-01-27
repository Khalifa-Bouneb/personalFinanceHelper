package com.finance.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import jakarta.validation.constraints.NotBlank;
import java.time.LocalDateTime;
import java.util.UUID;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Document(collection = "categories")
public class Category {
    
    @Id
    private String id = UUID.randomUUID().toString();
    
    @NotBlank(message = "Category name is required")
    private String name;
    
    private String color;
    
    private String icon;
    
    private LocalDateTime createdAt = LocalDateTime.now();
}
