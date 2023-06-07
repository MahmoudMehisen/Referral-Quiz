package com.mehisen.referralquizbackend.payload.mapper;

import com.mehisen.referralquizbackend.models.ReferralToken;
import com.mehisen.referralquizbackend.payload.dto.ReferralDto;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(MockitoExtension.class)
class ReferralMapperTest {

    @InjectMocks
    ReferralMapper referralMapper;
    @Test
    void toDto() {
        ReferralToken referralToken = new ReferralToken("token",true);

        ReferralDto referralDto = referralMapper.toDto(referralToken);

        assertEquals(referralDto.getToken(),referralToken.getToken());
    }
}