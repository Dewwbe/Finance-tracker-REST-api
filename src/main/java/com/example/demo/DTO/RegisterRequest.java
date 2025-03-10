package com.example.demo.DTO;

import lombok.Data;

@Data
public class RegisterRequest {
    private String username;
    private String email;
    private String password;

    public RegisterRequest(String username, String mail, String password) {
    }
}
