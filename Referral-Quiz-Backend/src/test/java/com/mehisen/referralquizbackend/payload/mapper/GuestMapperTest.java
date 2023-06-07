package com.mehisen.referralquizbackend.payload.mapper;

import com.mehisen.referralquizbackend.models.Guest;
import com.mehisen.referralquizbackend.payload.dto.GuestDto;
import com.mehisen.referralquizbackend.payload.mapper.GuestMapper;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.assertj.core.api.Assertions.assertThat;

@ExtendWith(MockitoExtension.class)
public class GuestMapperTest {
    @InjectMocks
    private GuestMapper guestMapper;

    @Test
    public void toDto() {
        Guest guest = Guest.builder()
                .phoneNumber("0123456789")
                .email("test@example.com")
                .totalPoints(100)
                .canPlay(true)
                .build();

        GuestDto guestDto = guestMapper.toDto(guest);

        assertThat(guestDto.getPhoneNumber()).isEqualTo(guest.getPhoneNumber());
        assertThat(guestDto.getEmail()).isEqualTo(guest.getEmail());
        assertThat(guestDto.getTotalPoints()).isEqualTo(guest.getTotalPoints());
        assertThat(guestDto.getCanPlay()).isEqualTo(guest.getCanPlay());
    }

    @Test
    public void fromDto() {
        GuestDto guestDto = GuestDto.builder()
                .phoneNumber("0123456789")
                .email("test@example.com")
                .totalPoints(100)
                .canPlay(true)
                .build();

        Guest guest = guestMapper.fromDto(guestDto);

        assertThat(guest.getPhoneNumber()).isEqualTo(guestDto.getPhoneNumber());
        assertThat(guest.getEmail()).isEqualTo(guestDto.getEmail());
        assertThat(guest.getTotalPoints()).isEqualTo(guestDto.getTotalPoints());
        assertThat(guest.getCanPlay()).isEqualTo(guestDto.getCanPlay());
    }
}
