package com.mehisen.referralquizbackend.services;

import com.mehisen.referralquizbackend.payload.request.AdminLoginRequest;
import com.mehisen.referralquizbackend.payload.resonse.JwtResponse;
import com.mehisen.referralquizbackend.repositories.UserRepository;
import com.mehisen.referralquizbackend.security.jwt.JwtUtils;
import com.mehisen.referralquizbackend.security.services.UserDetailsImpl;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@Slf4j
@RequiredArgsConstructor
public class AuthService {
    private final AuthenticationManager authenticationManager;

    private final JwtUtils jwtUtils;

    @Value("${mehisen.app.jwtExpirationMs}")
    private int jwtExpirationMs;

    public JwtResponse login(AdminLoginRequest loginRequest) {
        Authentication authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(loginRequest.getUsername(), loginRequest.getPassword()));

        SecurityContextHolder.getContext().setAuthentication(authentication);
        String jwt = jwtUtils.generateJwtTokenForAdminAuth(authentication);

        UserDetailsImpl userDetails = (UserDetailsImpl) authentication.getPrincipal();
        List<String> roles = userDetails.getAuthorities().stream()
                .map(GrantedAuthority::getAuthority).toList();

        String role = roles.get(0);

        return new JwtResponse(
                userDetails.getId(),
                jwt,
                userDetails.getUsername(),
                userDetails.getEmail(),
                role,
                jwtExpirationMs
        );
    }
}
