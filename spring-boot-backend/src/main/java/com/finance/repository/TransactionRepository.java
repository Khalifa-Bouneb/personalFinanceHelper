package com.finance.repository;

import com.finance.enums.TransactionSign;
import com.finance.model.Transaction;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;

@Repository
public interface TransactionRepository extends MongoRepository<Transaction, String> {
    
    List<Transaction> findByUserId(Integer userId);
    
    List<Transaction> findByCategoryId(String categoryId);
    
    List<Transaction> findBySign(TransactionSign sign);
    
    List<Transaction> findByUserIdAndSign(Integer userId, TransactionSign sign);
    
    List<Transaction> findByUserIdAndTransactionDateBetween(
            Integer userId, LocalDateTime start, LocalDateTime end);
    
    List<Transaction> findByUserIdAndSignAndTransactionDateBetween(
            Integer userId, TransactionSign sign, LocalDateTime start, LocalDateTime end);
}
