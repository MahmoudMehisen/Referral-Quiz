package com.mehisen.referralquizbackend.payload.mapper;

import com.mehisen.referralquizbackend.models.Redeem;
import com.mehisen.referralquizbackend.payload.dto.RedeemDto;
import org.springframework.stereotype.Component;

@Component
public class RedeemMapper {
    public RedeemDto toDto(Redeem redeem) {
        return RedeemDto.builder()
                .id(redeem.getId())
                .pointsToRedeem(redeem.getPointsToRedeem())
                .redeemName(redeem.getRedeemName())
                .isAvailable(redeem.isAvailable())
                .build();
    }

    public Redeem fromDto(RedeemDto redeemDto) {
        return Redeem.builder()
                .id(redeemDto.getId())
                .pointsToRedeem(redeemDto.getPointsToRedeem())
                .redeemName(redeemDto.getRedeemName())
                .isAvailable(redeemDto.isAvailable())
                .build();
    }
}
