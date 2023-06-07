package com.mehisen.referralquizbackend.services;

import com.mehisen.referralquizbackend.payload.request.AdminLoginRequest;
import com.mehisen.referralquizbackend.payload.resonse.JwtResponse;
import com.mehisen.referralquizbackend.security.jwt.JwtUtils;
import com.mehisen.referralquizbackend.security.services.UserDetailsImpl;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;

import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class AuthServiceTest {
    @Mock
    private AuthenticationManager authenticationManager;

    @Mock
    private JwtUtils jwtUtils;

    @InjectMocks
    private AuthService authService;


    @Test
    public void testLogin() {
        AdminLoginRequest loginRequest = new AdminLoginRequest();
        loginRequest.setUsername("admin");
        loginRequest.setPassword("adminPassword");

        Authentication authentication = mock(Authentication.class);
        when(authenticationManager.authenticate(any())).thenReturn(authentication);

        UserDetailsImpl userDetails = mock(UserDetailsImpl.class);
        when(authentication.getPrincipal()).thenReturn(userDetails);
        when(userDetails.getId()).thenReturn(1L);
        when(userDetails.getUsername()).thenReturn("admin");
        when(userDetails.getEmail()).thenReturn("admin@admin.com");

        List<GrantedAuthority> authorities = Arrays.stream("ROLE_ADMIN".split(","))
                .map(SimpleGrantedAuthority::new)
                .collect(Collectors.toList());
        when(userDetails.getAuthorities()).thenAnswer(invocation -> authorities);

        String jwtToken = "token123";
        when(jwtUtils.generateJwtTokenForAdminAuth(authentication)).thenReturn(jwtToken);

        JwtResponse response = authService.login(loginRequest);

        verify(authenticationManager).authenticate(
                new UsernamePasswordAuthenticationToken(loginRequest.getUsername(), loginRequest.getPassword()));

        verify(jwtUtils).generateJwtTokenForAdminAuth(authentication);

        assertEquals(1L, response.getId());
        assertEquals("admin", response.getUsername());
        assertEquals("admin@admin.com", response.getEmail());
        assertEquals("ROLE_ADMIN", response.getRole());
        assertEquals(jwtToken, response.getToken());
    }
}