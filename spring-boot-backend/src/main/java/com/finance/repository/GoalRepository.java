package com.finance.repository;

import com.finance.enums.GoalType;
import com.finance.model.Goal;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface GoalRepository extends MongoRepository<Goal, String> {
    
    List<Goal> findByUserId(Integer userId);
    
    List<Goal> findByCategoryId(String categoryId);
    
    List<Goal> findByType(GoalType type);
    
    List<Goal> findByUserIdAndType(Integer userId, GoalType type);
}
