package com.mehisen.referralquizbackend.payload.mapper;


import com.mehisen.referralquizbackend.models.ReferralToken;
import com.mehisen.referralquizbackend.payload.dto.ReferralDto;
import org.springframework.stereotype.Component;


@Component
public class ReferralMapper {
    public  ReferralDto toDto(ReferralToken referralToken) {
        return ReferralDto.builder()
                .token(referralToken.getToken())
                .build();
    }
}
