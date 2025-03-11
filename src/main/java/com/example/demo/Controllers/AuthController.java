package com.example.demo.Controllers;

import com.example.demo.Model.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

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

    // **NEW ENDPOINT: Get all users**
    @GetMapping("/users")
    public ResponseEntity<List<User>> getAllUsers() {
        return ResponseEntity.ok(authService.getAllUsers());
    }

    // **NEW ENDPOINT: Delete a user by ID**
    @DeleteMapping("/users/{id}")
    public ResponseEntity<String> deleteUser(@PathVariable String id) {
        return ResponseEntity.ok(authService.deleteUserById(id));
    }
}
