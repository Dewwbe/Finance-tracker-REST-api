package com.example.demo.DTO;

import lombok.Data;

@Data
public class AuthRequest {
    private String email;
    private String password;

    public AuthRequest(String mail, String password) {
    }

    public AuthRequest() {

    }
}
