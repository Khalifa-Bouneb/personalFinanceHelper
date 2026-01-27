package com.finance.controller;

import com.finance.model.Goal;
import com.finance.service.GoalService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import jakarta.validation.Valid;
import java.util.List;

@RestController
@RequestMapping("/api/goals")
@RequiredArgsConstructor
@CrossOrigin(origins = "http://localhost:4200")
public class GoalController {
    
    private final GoalService goalService;
    
    // GET /api/goals - Find all goals
    @GetMapping
    public ResponseEntity<List<Goal>> findAll() {
        return ResponseEntity.ok(goalService.findMany());
    }
    
    // GET /api/goals/{id} - Find one goal
    @GetMapping("/{id}")
    public ResponseEntity<Goal> findOne(@PathVariable String id) {
        return goalService.findOne(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }
    
    // POST /api/goals - Create one goal
    @PostMapping
    public ResponseEntity<Goal> createOne(@Valid @RequestBody Goal goal) {
        Goal created = goalService.createOne(goal);
        return ResponseEntity.status(HttpStatus.CREATED).body(created);
    }
    
    // PUT /api/goals/{id} - Update one goal
    @PutMapping("/{id}")
    public ResponseEntity<Goal> updateOne(@PathVariable String id, @RequestBody Goal goal) {
        return goalService.updateOne(id, goal)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }
    
    // DELETE /api/goals/{id} - Delete one goal
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteOne(@PathVariable String id) {
        boolean deleted = goalService.deleteOne(id);
        return deleted ? ResponseEntity.noContent().build() : ResponseEntity.notFound().build();
    }
}
