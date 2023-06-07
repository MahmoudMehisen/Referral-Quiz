package com.mehisen.referralquizbackend.payload.dto;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.*;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Builder
public class RedeemDto {
    @NotNull
    private Long id;
    @NotNull
    @Min(1)
    private Integer pointsToRedeem;
    @NotBlank
    private String redeemName;
    @NotNull
    private boolean isAvailable;
}
