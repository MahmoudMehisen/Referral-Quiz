package com.mehisen.referralquizbackend.payload.mapper;

import com.mehisen.referralquizbackend.models.Redeem;
import com.mehisen.referralquizbackend.payload.dto.RedeemDto;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.junit.jupiter.api.Assertions.assertEquals;

@ExtendWith(MockitoExtension.class)
class RedeemMapperTest {

    @InjectMocks
    private RedeemMapper redeemMapper;

    @Test
    public void toDto() {
        Redeem redeem = Redeem.builder()
                .id(1L)
                .pointsToRedeem(100)
                .redeemName("Sample Redeem")
                .isAvailable(true)
                .build();

        RedeemDto redeemDto = redeemMapper.toDto(redeem);

        assertEquals(redeem.getId(), redeemDto.getId());
        assertEquals(redeem.getPointsToRedeem(), redeemDto.getPointsToRedeem());
        assertEquals(redeem.getRedeemName(), redeemDto.getRedeemName());
        assertEquals(redeem.isAvailable(), redeemDto.isAvailable());
    }

    @Test
    public void fromDto() {
        RedeemDto redeemDto = RedeemDto.builder()
                .id(1L)
                .pointsToRedeem(100)
                .redeemName("Sample Redeem")
                .isAvailable(true)
                .build();

        Redeem redeem = redeemMapper.fromDto(redeemDto);

        assertEquals(redeemDto.getId(), redeem.getId());
        assertEquals(redeemDto.getPointsToRedeem(), redeem.getPointsToRedeem());
        assertEquals(redeemDto.getRedeemName(), redeem.getRedeemName());
        assertEquals(redeemDto.isAvailable(), redeem.isAvailable());
    }
}