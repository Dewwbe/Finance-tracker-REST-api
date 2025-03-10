package com.example.demo.Service;

import com.example.demo.DTO.AuthRequest;
import com.example.demo.DTO.AuthResponse;
import com.example.demo.DTO.RegisterRequest;
import com.example.demo.Model.User;
import com.example.demo.Repository.UserRepository;
import com.example.demo.Utils.JwtUtil;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class AuthServiceTest {

    @InjectMocks
    private AuthService authService;

    @Mock
    private UserRepository userRepository;

    @Mock
    private JwtUtil jwtUtil;

    @Mock
    private PasswordEncoder passwordEncoder;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }




    @Test
    void testRegisterUserAlreadyExists() {
        RegisterRequest request = new RegisterRequest("username", "email@example.com", "password");

        when(userRepository.findByEmail(request.getEmail())).thenReturn(Optional.of(new User()));

        RuntimeException thrown = assertThrows(RuntimeException.class, () -> authService.register(request));
        assertEquals("Email already exists", thrown.getMessage());
        verify(userRepository, times(0)).save(any(User.class));
    }



    @Test
    void testLoginUserNotFound() {
        AuthRequest request = new AuthRequest("email@example.com", "password");

        when(userRepository.findByEmail(request.getEmail())).thenReturn(Optional.empty());

        RuntimeException thrown = assertThrows(RuntimeException.class, () -> authService.login(request));
        assertEquals("User not found", thrown.getMessage());
    }


}
