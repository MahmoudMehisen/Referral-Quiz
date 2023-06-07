package com.mehisen.referralquizbackend.controllers;


import com.mehisen.referralquizbackend.payload.request.AdminLoginRequest;
import com.mehisen.referralquizbackend.payload.resonse.JwtResponse;
import com.mehisen.referralquizbackend.repositories.UserRepository;
import com.mehisen.referralquizbackend.security.jwt.JwtUtils;
import com.mehisen.referralquizbackend.security.services.UserDetailsImpl;
import com.mehisen.referralquizbackend.services.AuthService;
import com.mehisen.referralquizbackend.services.OTPService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/auth")
@RequiredArgsConstructor
public class AuthController {

    private final AuthService authService;

    @PostMapping("/login")
    public ResponseEntity<?> authenticateUser(@Valid @RequestBody AdminLoginRequest loginRequest) {
        JwtResponse jwtResponse = authService.login(loginRequest);
        return ResponseEntity.ok(jwtResponse);
    }
}
