package com.finance.controller;

import com.finance.model.Item;
import com.finance.service.ItemService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import jakarta.validation.Valid;
import java.util.List;

@RestController
@RequestMapping("/api/items")
@RequiredArgsConstructor
@CrossOrigin(origins = "http://localhost:4200")
public class ItemController {
    
    private final ItemService itemService;
    
    // GET /api/items - Find all items
    @GetMapping
    public ResponseEntity<List<Item>> findAll() {
        return ResponseEntity.ok(itemService.findMany());
    }
    
    // GET /api/items/{id} - Find one item
    @GetMapping("/{id}")
    public ResponseEntity<Item> findOne(@PathVariable String id) {
        return itemService.findOne(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }
    
    // POST /api/items - Create one item
    @PostMapping
    public ResponseEntity<Item> createOne(@Valid @RequestBody Item item) {
        Item created = itemService.createOne(item);
        return ResponseEntity.status(HttpStatus.CREATED).body(created);
    }
    
    // PUT /api/items/{id} - Update one item
    @PutMapping("/{id}")
    public ResponseEntity<Item> updateOne(@PathVariable String id, @RequestBody Item item) {
        return itemService.updateOne(id, item)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }
    
    // DELETE /api/items/{id} - Delete one item
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteOne(@PathVariable String id) {
        boolean deleted = itemService.deleteOne(id);
        return deleted ? ResponseEntity.noContent().build() : ResponseEntity.notFound().build();
    }
}
