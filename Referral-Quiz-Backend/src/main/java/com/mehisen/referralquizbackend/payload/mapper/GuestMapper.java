package com.mehisen.referralquizbackend.payload.mapper;

import com.mehisen.referralquizbackend.models.Guest;
import com.mehisen.referralquizbackend.payload.dto.GuestDto;
import org.springframework.stereotype.Component;

@Component
public class GuestMapper {
    public GuestDto toDto(Guest guest) {
        return GuestDto.builder()
                .phoneNumber(guest.getPhoneNumber())
                .email(guest.getEmail())
                .totalPoints(guest.getTotalPoints())
                .canPlay(guest.getCanPlay())
                .build();
    }

    public Guest fromDto(GuestDto guestDto) {
        return Guest.builder()
                .phoneNumber(guestDto.getPhoneNumber())
                .email(guestDto.getEmail())
                .totalPoints(guestDto.getTotalPoints())
                .canPlay(guestDto.getCanPlay())
                .build();
    }
}
