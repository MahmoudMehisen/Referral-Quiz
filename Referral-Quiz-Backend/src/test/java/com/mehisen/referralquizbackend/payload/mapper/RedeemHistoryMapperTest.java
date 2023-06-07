package com.mehisen.referralquizbackend.payload.mapper;

import com.mehisen.referralquizbackend.models.Guest;
import com.mehisen.referralquizbackend.models.Redeem;
import com.mehisen.referralquizbackend.models.RedeemHistory;
import com.mehisen.referralquizbackend.payload.dto.RedeemDto;
import com.mehisen.referralquizbackend.payload.dto.RedeemHistoryDto;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.stereotype.Component;

import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(MockitoExtension.class)
class RedeemHistoryMapperTest {

    @InjectMocks
    RedeemHistoryMapper redeemHistoryMapper;
    @Test
    void toDto() {
        RedeemHistory redeemHistory = RedeemHistory.builder()
                .id(1L)
                .pointsForRedeem(100)
                .redeem(Redeem.builder().redeemName("name").build())
                .guest(Guest.builder().phoneNumber("123").build())
                .build();

        RedeemHistoryDto redeemHistoryDto = redeemHistoryMapper.toDto(redeemHistory);

        assertEquals(redeemHistory.getId(), redeemHistoryDto.getId());
        assertEquals(redeemHistory.getPointsForRedeem(), redeemHistoryDto.getPointsForRedeem());
        assertEquals(redeemHistory.getRedeem().getRedeemName(), redeemHistoryDto.getRedeemName());
        assertEquals(redeemHistory.getGuest().getPhoneNumber(), redeemHistoryDto.getGuestPhoneNumber());
    }
}