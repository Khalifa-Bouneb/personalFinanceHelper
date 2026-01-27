package com.finance.service;

import com.finance.model.Category;
import com.finance.model.Goal;
import com.finance.model.User;
import com.finance.repository.CategoryRepository;
import com.finance.repository.GoalRepository;
import com.finance.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class GoalService {
    
    private final GoalRepository goalRepository;
    private final UserRepository userRepository;
    private final CategoryRepository categoryRepository;
    
    // Find one goal by ID
    public Optional<Goal> findOne(String id) {
        return goalRepository.findById(id).map(this::populateReferences);
    }
    
    // Find many (all) goals
    public List<Goal> findMany() {
        return goalRepository.findAll().stream()
                .map(this::populateReferences)
                .toList();
    }
    
    // Create one goal
    public Goal createOne(Goal goal) {
        // Set references if IDs are provided
        if (goal.getUserId() != null) {
            userRepository.findById(goal.getUserId()).ifPresent(goal::setUser);
        }
        if (goal.getCategoryId() != null) {
            categoryRepository.findById(goal.getCategoryId()).ifPresent(goal::setCategory);
        }
        return goalRepository.save(goal);
    }
    
    // Update one goal
    public Optional<Goal> updateOne(String id, Goal goalDetails) {
        return goalRepository.findById(id).map(existingGoal -> {
            if (goalDetails.getMaxAmount() != null) {
                existingGoal.setMaxAmount(goalDetails.getMaxAmount());
            }
            if (goalDetails.getType() != null) {
                existingGoal.setType(goalDetails.getType());
            }
            if (goalDetails.getStartDate() != null) {
                existingGoal.setStartDate(goalDetails.getStartDate());
            }
            if (goalDetails.getEndDate() != null) {
                existingGoal.setEndDate(goalDetails.getEndDate());
            }
            if (goalDetails.getUserId() != null) {
                existingGoal.setUserId(goalDetails.getUserId());
                userRepository.findById(goalDetails.getUserId())
                        .ifPresent(existingGoal::setUser);
            }
            if (goalDetails.getCategoryId() != null) {
                existingGoal.setCategoryId(goalDetails.getCategoryId());
                categoryRepository.findById(goalDetails.getCategoryId())
                        .ifPresent(existingGoal::setCategory);
            }
            return goalRepository.save(existingGoal);
        });
    }
    
    // Delete one goal
    public boolean deleteOne(String id) {
        if (goalRepository.existsById(id)) {
            goalRepository.deleteById(id);
            return true;
        }
        return false;
    }
    
    // Helper method to populate references
    private Goal populateReferences(Goal goal) {
        if (goal.getUserId() != null) {
            userRepository.findById(goal.getUserId()).ifPresent(goal::setUser);
        }
        if (goal.getCategoryId() != null) {
            categoryRepository.findById(goal.getCategoryId()).ifPresent(goal::setCategory);
        }
        return goal;
    }
}
