package com.example.demo.Controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/auth")
public class AuthController {

    @Autowired
    private com.example.demo.Service.AuthService authService;

    @PostMapping("/register")
    public ResponseEntity<String> register(@RequestBody com.example.demo.DTO.RegisterRequest request) {
        return ResponseEntity.ok(authService.register(request));
    }

    @PostMapping("/login")
    public ResponseEntity<com.example.demo.DTO.AuthResponse> login(@RequestBody com.example.demo.DTO.AuthRequest request) {
        return ResponseEntity.ok(authService.login(request));
    }
}
