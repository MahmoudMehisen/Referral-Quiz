package com.mehisen.referralquizbackend.payload.mapper;

import com.mehisen.referralquizbackend.models.RedeemHistory;
import com.mehisen.referralquizbackend.payload.dto.RedeemHistoryDto;
import org.springframework.stereotype.Component;

@Component
public class RedeemHistoryMapper {
    public RedeemHistoryDto toDto(RedeemHistory redeemsHistory) {
        return RedeemHistoryDto.builder()
                .id(redeemsHistory.getId())
                .guestPhoneNumber(redeemsHistory.getGuest().getPhoneNumber())
                .pointsForRedeem(redeemsHistory.getPointsForRedeem())
                .redeemName(redeemsHistory.getRedeem().getRedeemName())
                .build();
    }
}
