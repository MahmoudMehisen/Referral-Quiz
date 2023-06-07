package com.mehisen.referralquizbackend.payload.request;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class AddRedeemRequest {
    @NotBlank
    private String redeemName;
    @NotNull
    @Min(1)
    private Integer pointsForRedeem;
}
