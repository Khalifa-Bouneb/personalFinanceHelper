package com.finance.controller;

import com.finance.model.Transaction;
import com.finance.service.TransactionService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import jakarta.validation.Valid;
import java.util.List;

@RestController
@RequestMapping("/api/transactions")
@RequiredArgsConstructor
@CrossOrigin(origins = "http://localhost:4200")
public class TransactionController {
    
    private final TransactionService transactionService;
    
    // GET /api/transactions - Find all transactions
    @GetMapping
    public ResponseEntity<List<Transaction>> findAll() {
        return ResponseEntity.ok(transactionService.findMany());
    }
    
    // GET /api/transactions/{id} - Find one transaction
    @GetMapping("/{id}")
    public ResponseEntity<Transaction> findOne(@PathVariable String id) {
        return transactionService.findOne(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }
    
    // POST /api/transactions - Create one transaction
    @PostMapping
    public ResponseEntity<Transaction> createOne(@Valid @RequestBody Transaction transaction) {
        Transaction created = transactionService.createOne(transaction);
        return ResponseEntity.status(HttpStatus.CREATED).body(created);
    }
    
    // PUT /api/transactions/{id} - Update one transaction
    @PutMapping("/{id}")
    public ResponseEntity<Transaction> updateOne(@PathVariable String id, @RequestBody Transaction transaction) {
        return transactionService.updateOne(id, transaction)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }
    
    // DELETE /api/transactions/{id} - Delete one transaction
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteOne(@PathVariable String id) {
        boolean deleted = transactionService.deleteOne(id);
        return deleted ? ResponseEntity.noContent().build() : ResponseEntity.notFound().build();
    }
}
