package com.mehisen.referralquizbackend.payload.dto;

import com.mehisen.referralquizbackend.models.Guest;
import lombok.*;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Builder
public class GuestDto {
    private String phoneNumber;
    private String email;
    private Integer totalPoints;
    private Boolean canPlay;
}
