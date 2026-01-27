package com.finance.service;

import com.finance.model.Category;
import com.finance.repository.CategoryRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class CategoryService {
    
    private final CategoryRepository categoryRepository;
    
    // Find one category by ID
    public Optional<Category> findOne(String id) {
        return categoryRepository.findById(id);
    }
    
    // Find many (all) categories
    public List<Category> findMany() {
        return categoryRepository.findAll();
    }
    
    // Create one category
    public Category createOne(Category category) {
        return categoryRepository.save(category);
    }
    
    // Update one category
    public Optional<Category> updateOne(String id, Category categoryDetails) {
        return categoryRepository.findById(id).map(existingCategory -> {
            if (categoryDetails.getName() != null) {
                existingCategory.setName(categoryDetails.getName());
            }
            if (categoryDetails.getColor() != null) {
                existingCategory.setColor(categoryDetails.getColor());
            }
            if (categoryDetails.getIcon() != null) {
                existingCategory.setIcon(categoryDetails.getIcon());
            }
            return categoryRepository.save(existingCategory);
        });
    }
    
    // Delete one category
    public boolean deleteOne(String id) {
        if (categoryRepository.existsById(id)) {
            categoryRepository.deleteById(id);
            return true;
        }
        return false;
    }
}
