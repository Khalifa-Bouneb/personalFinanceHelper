package com.finance.service;

import com.finance.model.Category;
import com.finance.model.Item;
import com.finance.model.Transaction;
import com.finance.model.User;
import com.finance.repository.CategoryRepository;
import com.finance.repository.ItemRepository;
import com.finance.repository.TransactionRepository;
import com.finance.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class TransactionService {
    
    private final TransactionRepository transactionRepository;
    private final UserRepository userRepository;
    private final CategoryRepository categoryRepository;
    private final ItemRepository itemRepository;
    
    // Find one transaction by ID
    public Optional<Transaction> findOne(String id) {
        return transactionRepository.findById(id).map(this::populateReferences);
    }
    
    // Find many (all) transactions
    public List<Transaction> findMany() {
        return transactionRepository.findAll().stream()
                .map(this::populateReferences)
                .toList();
    }
    
    // Create one transaction
    public Transaction createOne(Transaction transaction) {
        // Set references if IDs are provided
        if (transaction.getUserId() != null) {
            userRepository.findById(transaction.getUserId()).ifPresent(transaction::setUser);
        }
        if (transaction.getCategoryId() != null) {
            categoryRepository.findById(transaction.getCategoryId()).ifPresent(transaction::setCategory);
        }
        if (transaction.getItemId() != null) {
            itemRepository.findById(transaction.getItemId()).ifPresent(transaction::setItem);
        }
        return transactionRepository.save(transaction);
    }
    
    // Update one transaction
    public Optional<Transaction> updateOne(String id, Transaction transactionDetails) {
        return transactionRepository.findById(id).map(existingTransaction -> {
            if (transactionDetails.getAmount() != null) {
                existingTransaction.setAmount(transactionDetails.getAmount());
            }
            if (transactionDetails.getSign() != null) {
                existingTransaction.setSign(transactionDetails.getSign());
            }
            if (transactionDetails.getTransactionDate() != null) {
                existingTransaction.setTransactionDate(transactionDetails.getTransactionDate());
            }
            if (transactionDetails.getUserId() != null) {
                existingTransaction.setUserId(transactionDetails.getUserId());
                userRepository.findById(transactionDetails.getUserId())
                        .ifPresent(existingTransaction::setUser);
            }
            if (transactionDetails.getCategoryId() != null) {
                existingTransaction.setCategoryId(transactionDetails.getCategoryId());
                categoryRepository.findById(transactionDetails.getCategoryId())
                        .ifPresent(existingTransaction::setCategory);
            }
            if (transactionDetails.getItemId() != null) {
                existingTransaction.setItemId(transactionDetails.getItemId());
                itemRepository.findById(transactionDetails.getItemId())
                        .ifPresent(existingTransaction::setItem);
            }
            return transactionRepository.save(existingTransaction);
        });
    }
    
    // Delete one transaction
    public boolean deleteOne(String id) {
        if (transactionRepository.existsById(id)) {
            transactionRepository.deleteById(id);
            return true;
        }
        return false;
    }
    
    // Helper method to populate references
    private Transaction populateReferences(Transaction transaction) {
        if (transaction.getUserId() != null) {
            userRepository.findById(transaction.getUserId()).ifPresent(transaction::setUser);
        }
        if (transaction.getCategoryId() != null) {
            categoryRepository.findById(transaction.getCategoryId()).ifPresent(transaction::setCategory);
        }
        if (transaction.getItemId() != null) {
            itemRepository.findById(transaction.getItemId()).ifPresent(transaction::setItem);
        }
        return transaction;
    }
}
