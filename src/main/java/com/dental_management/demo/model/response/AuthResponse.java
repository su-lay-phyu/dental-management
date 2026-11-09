package com.dental_management.demo.model.response;

import lombok.*;

@Getter
@Setter
@AllArgsConstructor
public class AuthResponse {
    private String token;
    private Long userId;
    private String name;
    private String email;
    private String role;

    public AuthResponse(String token,String email, String role) {
        this.token = token;
        this.email = email;
        this.role = role;
    }
}
