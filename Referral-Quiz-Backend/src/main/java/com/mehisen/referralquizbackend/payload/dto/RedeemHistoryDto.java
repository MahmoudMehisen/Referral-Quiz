package com.mehisen.referralquizbackend.payload.dto;

import lombok.*;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Builder
public class RedeemHistoryDto {
    private Long id;
    private String guestPhoneNumber;
    private String redeemName;
    private Integer pointsForRedeem;
}
