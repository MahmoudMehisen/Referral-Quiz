package com.mehisen.referralquizbackend.payload.mapper;

import com.mehisen.referralquizbackend.models.Metadata;
import com.mehisen.referralquizbackend.payload.dto.MetadataDto;
import org.springframework.stereotype.Component;

@Component
public class MetadataMapper {

    public MetadataDto toDto(Metadata metadata) {
        return MetadataDto.builder()
                .id(metadata.getId())
                .pointsPerReferral(metadata.getPointsPerReferral())
                .referralExpirationTime(metadata.getReferralExpirationTime())
                .activeGroupId(metadata.getActiveGroupId())
                .canUserDoReferral(metadata.isCanUserDoReferral())
                .pointsPerQuestion(metadata.getPointsPerQuestion())
                .build();
    }

    public Metadata fromDto(MetadataDto metadataDto) {
        return Metadata.builder()
                .id(metadataDto.getId())
                .pointsPerReferral(metadataDto.getPointsPerReferral())
                .referralExpirationTime(metadataDto.getReferralExpirationTime())
                .activeGroupId(metadataDto.getActiveGroupId())
                .canUserDoReferral(metadataDto.isCanUserDoReferral())
                .pointsPerQuestion(metadataDto.getPointsPerQuestion())
                .build();
    }
}
