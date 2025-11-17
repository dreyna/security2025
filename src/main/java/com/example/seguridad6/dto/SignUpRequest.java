package com.example.seguridad6.dto;

import lombok.Data;

import java.util.Set;

@Data
public class SignUpRequest {
    private String username;
    private String password;
    private Set<String> roles;  // “admin”, “user”
}