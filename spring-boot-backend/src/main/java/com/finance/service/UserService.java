package com.finance.service;

import com.finance.model.User;
import com.finance.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class UserService {
    
    private final UserRepository userRepository;
    
    // Find one user by ID
    public Optional<User> findOne(Integer id) {
        return userRepository.findById(id);
    }
    
    // Find many (all) users
    public List<User> findMany() {
        return userRepository.findAll();
    }
    
    // Create one user
    public User createOne(User user) {
        // Auto-generate ID if not provided
        if (user.getId() == null) {
            user.setId(generateNextUserId());
        }
        return userRepository.save(user);
    }
    
    // Update one user
    public Optional<User> updateOne(Integer id, User userDetails) {
        return userRepository.findById(id).map(existingUser -> {
            if (userDetails.getName() != null) {
                existingUser.setName(userDetails.getName());
            }
            if (userDetails.getEmail() != null) {
                existingUser.setEmail(userDetails.getEmail());
            }
            if (userDetails.getPasswordHash() != null) {
                existingUser.setPasswordHash(userDetails.getPasswordHash());
            }
            if (userDetails.getCurrency() != null) {
                existingUser.setCurrency(userDetails.getCurrency());
            }
            return userRepository.save(existingUser);
        });
    }
    
    // Delete one user
    public boolean deleteOne(Integer id) {
        if (userRepository.existsById(id)) {
            userRepository.deleteById(id);
            return true;
        }
        return false;
    }
    
    // Helper method to generate next user ID
    private Integer generateNextUserId() {
        List<User> users = userRepository.findAll();
        return users.stream()
                .mapToInt(User::getId)
                .max()
                .orElse(0) + 1;
    }
}
