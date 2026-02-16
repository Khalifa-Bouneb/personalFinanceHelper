package com.finance.controller;

import com.finance.dto.AuthResponse;
import com.finance.dto.LoginRequest;
import com.finance.dto.RegisterRequest;
import com.finance.model.User;
import com.finance.repository.UserRepository;
import com.finance.security.JwtUtil;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/auth")
@RequiredArgsConstructor
@CrossOrigin(origins = "http://localhost:4200")
public class AuthController {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtUtil jwtUtil;

    @PostMapping("/register")
    public ResponseEntity<?> register(@Valid @RequestBody RegisterRequest request) {
        if (userRepository.existsByEmail(request.getEmail())) {
            return ResponseEntity.badRequest()
                    .body(Map.of("message", "Email already registered"));
        }

        User user = new User();
        user.setId(generateNextUserId());
        user.setName(request.getName());
        user.setEmail(request.getEmail());
        user.setPasswordHash(passwordEncoder.encode(request.getPassword()));
        user.setCurrency(request.getCurrency());
        user.setCreatedAt(LocalDateTime.now());

        User saved = userRepository.save(user);
        String token = jwtUtil.generateToken(saved.getEmail(), saved.getId());

        return ResponseEntity.status(HttpStatus.CREATED)
                .body(new AuthResponse(token, saved.getId(), saved.getName(),
                        saved.getEmail(), saved.getCurrency()));
    }

    @PostMapping("/login")
    public ResponseEntity<?> login(@Valid @RequestBody LoginRequest request) {
        return userRepository.findByEmail(request.getEmail())
                .map(user -> {
                    if (passwordEncoder.matches(request.getPassword(), user.getPasswordHash())) {
                        String token = jwtUtil.generateToken(user.getEmail(), user.getId());
                        return ResponseEntity.ok(
                                new AuthResponse(token, user.getId(), user.getName(),
                                        user.getEmail(), user.getCurrency()));
                    }
                    return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                            .body((Object) Map.of("message", "Invalid credentials"));
                })
                .orElse(ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                        .body(Map.of("message", "Invalid credentials")));
    }

    private Integer generateNextUserId() {
        List<User> users = userRepository.findAll();
        return users.stream()
                .mapToInt(User::getId)
                .max()
                .orElse(0) + 1;
    }
}
