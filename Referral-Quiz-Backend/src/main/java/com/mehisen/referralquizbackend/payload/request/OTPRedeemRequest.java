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
public class OTPRedeemRequest {
    @NotBlank
    private String token;
    @NotBlank
    private String phoneNumber;
}
