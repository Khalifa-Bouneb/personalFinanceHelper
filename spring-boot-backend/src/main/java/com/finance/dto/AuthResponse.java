package com.finance.dto;

import com.finance.model.User;
import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class AuthResponse {
    private String token;
    private Integer userId;
    private String name;
    private String email;
    private String currency;
}
