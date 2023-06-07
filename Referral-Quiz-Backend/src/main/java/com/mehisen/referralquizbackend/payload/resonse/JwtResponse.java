package com.mehisen.referralquizbackend.payload.resonse;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class JwtResponse {
    private Long id;
    private String token;
    private String username;
    private String email;
    private String role;
    private int expiresIn;
}
