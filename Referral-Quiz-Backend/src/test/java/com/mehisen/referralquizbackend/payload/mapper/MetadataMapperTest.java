package com.mehisen.referralquizbackend.payload.mapper;

import com.mehisen.referralquizbackend.models.Metadata;
import com.mehisen.referralquizbackend.payload.dto.MetadataDto;
import com.mehisen.referralquizbackend.payload.mapper.MetadataMapper;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.assertj.core.api.Assertions.assertThat;
@ExtendWith(MockitoExtension.class)
public class MetadataMapperTest {
    @InjectMocks
    private MetadataMapper metadataMapper;

    @Test
    public void toDto() {
        Metadata metadata = Metadata.builder()
                .id(1L)
                .pointsPerReferral(10)
                .referralExpirationTime(30)
                .activeGroupId(2L)
                .canUserDoReferral(true)
                .pointsPerQuestion(5)
                .build();

        MetadataDto metadataDto = metadataMapper.toDto(metadata);

        assertThat(metadataDto.getId()).isEqualTo(metadata.getId());
        assertThat(metadataDto.getPointsPerReferral()).isEqualTo(metadata.getPointsPerReferral());
        assertThat(metadataDto.getReferralExpirationTime()).isEqualTo(metadata.getReferralExpirationTime());
        assertThat(metadataDto.getActiveGroupId()).isEqualTo(metadata.getActiveGroupId());
        assertThat(metadataDto.isCanUserDoReferral()).isEqualTo(metadata.isCanUserDoReferral());
        assertThat(metadataDto.getPointsPerQuestion()).isEqualTo(metadata.getPointsPerQuestion());
    }

    @Test
    public void fromDto() {
        MetadataDto metadataDto = MetadataDto.builder()
                .id(1L)
                .pointsPerReferral(10)
                .referralExpirationTime(30)
                .activeGroupId(2L)
                .canUserDoReferral(true)
                .pointsPerQuestion(5)
                .build();

        Metadata metadata = metadataMapper.fromDto(metadataDto);

        assertThat(metadata.getId()).isEqualTo(metadataDto.getId());
        assertThat(metadata.getPointsPerReferral()).isEqualTo(metadataDto.getPointsPerReferral());
        assertThat(metadata.getReferralExpirationTime()).isEqualTo(metadataDto.getReferralExpirationTime());
        assertThat(metadata.getActiveGroupId()).isEqualTo(metadataDto.getActiveGroupId());
        assertThat(metadata.isCanUserDoReferral()).isEqualTo(metadataDto.isCanUserDoReferral());
        assertThat(metadata.getPointsPerQuestion()).isEqualTo(metadataDto.getPointsPerQuestion());
    }
}
