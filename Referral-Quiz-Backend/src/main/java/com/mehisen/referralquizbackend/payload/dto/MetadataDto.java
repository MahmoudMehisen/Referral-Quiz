package com.mehisen.referralquizbackend.payload.dto;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import lombok.*;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Builder
public class MetadataDto {
    private Long id;
    @NotNull
    @Min(1)
    private Integer pointsPerQuestion;
    @NotNull
    @Min(1)
    private Integer pointsPerReferral;
    @NotNull
    @Min(1)
    private Integer referralExpirationTime;
    @NotNull
    private boolean canUserDoReferral;
    private Long activeGroupId;
}
