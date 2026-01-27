package com.finance.controller;

import com.finance.model.User;
import com.finance.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import jakarta.validation.Valid;
import java.util.List;

@RestController
@RequestMapping("/api/users")
@RequiredArgsConstructor
@CrossOrigin(origins = "http://localhost:4200")
public class UserController {
    
    private final UserService userService;
    
    // GET /api/users - Find all users
    @GetMapping
    public ResponseEntity<List<User>> findAll() {
        return ResponseEntity.ok(userService.findMany());
    }
    
    // GET /api/users/{id} - Find one user
    @GetMapping("/{id}")
    public ResponseEntity<User> findOne(@PathVariable Integer id) {
        return userService.findOne(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }
    
    // POST /api/users - Create one user
    @PostMapping
    public ResponseEntity<User> createOne(@Valid @RequestBody User user) {
        User created = userService.createOne(user);
        return ResponseEntity.status(HttpStatus.CREATED).body(created);
    }
    
    // PUT /api/users/{id} - Update one user
    @PutMapping("/{id}")
    public ResponseEntity<User> updateOne(@PathVariable Integer id, @RequestBody User user) {
        return userService.updateOne(id, user)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }
    
    // DELETE /api/users/{id} - Delete one user
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteOne(@PathVariable Integer id) {
        boolean deleted = userService.deleteOne(id);
        return deleted ? ResponseEntity.noContent().build() : ResponseEntity.notFound().build();
    }
}
