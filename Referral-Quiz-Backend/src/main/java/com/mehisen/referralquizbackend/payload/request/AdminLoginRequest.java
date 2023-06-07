package com.mehisen.referralquizbackend.payload.request;

import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class AdminLoginRequest {
    @NotBlank(message = "Username shouldn't be empty")
    private String username;
    @NotBlank(message = "password shouldn't be empty")
    private String password;
}
