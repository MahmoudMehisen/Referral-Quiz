package com.mehisen.referralquizbackend.payload.request;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class GuestInfoRequest {
    @NotBlank(message = "Phone number shouldn't be empty")
    private String phoneNumber;
    @Email
    private String email;
}
