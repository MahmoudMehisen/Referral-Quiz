package com.mehisen.referralquizbackend.payload.request;


import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.*;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Builder
public class ReferralRequest {
    @NotBlank
    private String newGuestPhone;
    @NotNull
    private String currentGuestPhone;
    private Boolean fromAdmin;
}
