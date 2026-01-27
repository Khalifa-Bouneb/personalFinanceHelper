package com.finance.controller;

import com.finance.model.Category;
import com.finance.service.CategoryService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import jakarta.validation.Valid;
import java.util.List;

@RestController
@RequestMapping("/api/categories")
@RequiredArgsConstructor
@CrossOrigin(origins = "http://localhost:4200")
public class CategoryController {
    
    private final CategoryService categoryService;
    
    // GET /api/categories - Find all categories
    @GetMapping
    public ResponseEntity<List<Category>> findAll() {
        return ResponseEntity.ok(categoryService.findMany());
    }
    
    // GET /api/categories/{id} - Find one category
    @GetMapping("/{id}")
    public ResponseEntity<Category> findOne(@PathVariable String id) {
        return categoryService.findOne(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }
    
    // POST /api/categories - Create one category
    @PostMapping
    public ResponseEntity<Category> createOne(@Valid @RequestBody Category category) {
        Category created = categoryService.createOne(category);
        return ResponseEntity.status(HttpStatus.CREATED).body(created);
    }
    
    // PUT /api/categories/{id} - Update one category
    @PutMapping("/{id}")
    public ResponseEntity<Category> updateOne(@PathVariable String id, @RequestBody Category category) {
        return categoryService.updateOne(id, category)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }
    
    // DELETE /api/categories/{id} - Delete one category
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteOne(@PathVariable String id) {
        boolean deleted = categoryService.deleteOne(id);
        return deleted ? ResponseEntity.noContent().build() : ResponseEntity.notFound().build();
    }
}
