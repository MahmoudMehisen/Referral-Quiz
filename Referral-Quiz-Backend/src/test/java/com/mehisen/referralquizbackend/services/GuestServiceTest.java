package com.mehisen.referralquizbackend.services;

import com.mehisen.referralquizbackend.exception.DuplicateGuestEmailException;
import com.mehisen.referralquizbackend.exception.GuestNotFoundException;
import com.mehisen.referralquizbackend.exception.ReferralTokenExpiredException;
import com.mehisen.referralquizbackend.exception.ReferralTokenNotFoundException;
import com.mehisen.referralquizbackend.models.Guest;
import com.mehisen.referralquizbackend.models.Metadata;
import com.mehisen.referralquizbackend.models.ReferralToken;
import com.mehisen.referralquizbackend.payload.dto.GuestDto;
import com.mehisen.referralquizbackend.payload.dto.MetadataDto;
import com.mehisen.referralquizbackend.payload.mapper.GuestMapper;
import com.mehisen.referralquizbackend.payload.mapper.MetadataMapper;
import com.mehisen.referralquizbackend.payload.request.AcceptReferralRequest;
import com.mehisen.referralquizbackend.payload.request.GuestInfoRequest;
import com.mehisen.referralquizbackend.payload.request.ReferralRequest;
import com.mehisen.referralquizbackend.repositories.GuestRepository;
import com.mehisen.referralquizbackend.repositories.MetadataRepository;
import com.mehisen.referralquizbackend.repositories.OtpRedeemRepository;
import com.mehisen.referralquizbackend.repositories.ReferralTokenRepository;
import com.mehisen.referralquizbackend.security.jwt.JwtUtils;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;
import static org.assertj.core.api.Assertions.assertThat;


@ExtendWith(MockitoExtension.class)
class GuestServiceTest {


    @Mock
    private JwtUtils jwtUtils;

    @Mock
    private ReferralTokenRepository referralTokenRepository;

    @Mock
    private GuestRepository guestRepository;

    @Mock
    private MetadataRepository metadataRepository;

    @Mock
    private GuestMapper guestMapper;

    @Mock
    private MetadataMapper metadataMapper;

    @Mock
    private OtpRedeemRepository otpRedeemRepository;

    @InjectMocks
    private GuestService guestService;


    @Test
    void testAcceptReferralWhenTokenValidFromAdminWithCurrentGuest() {
        String token = "token123";
        String newPhoneNumber = "123";
        AcceptReferralRequest acceptReferralRequest = new AcceptReferralRequest(token, newPhoneNumber);
        ReferralRequest referralRequest = new ReferralRequest(newPhoneNumber, "0", true);
        ReferralToken referralToken = new ReferralToken(token, true);
        Guest newGuest = new Guest(newPhoneNumber, null, 0, true);

        when(referralTokenRepository.findById(token)).thenReturn(Optional.of(referralToken));

        when(jwtUtils.validateJwtToken(referralToken.getToken())).thenReturn(true);
        when(jwtUtils.getReferralRequestFromJwtToken(referralToken.getToken())).thenReturn(referralRequest);

        when(guestRepository.findById(referralRequest.getNewGuestPhone())).thenReturn(Optional.of(newGuest));

        GuestDto guestDto = guestService.acceptReferral(acceptReferralRequest);

        verify(referralTokenRepository).save(referralToken);
        verify(guestRepository, times(1)).save(any(Guest.class));
    }

    @Test
    void testAcceptReferralWhenTokenValid() {
        String token = "token123";
        String newPhoneNumber = "123";
        String currentPhoneNumber = "456";
        AcceptReferralRequest acceptReferralRequest = new AcceptReferralRequest(token, newPhoneNumber);
        ReferralRequest referralRequest = new ReferralRequest(newPhoneNumber, currentPhoneNumber, false);
        ReferralToken referralToken = new ReferralToken(token, true);
        Guest existingGuest = new Guest(currentPhoneNumber, "mail@mail.com", 20, false);
        GuestDto newGuestDto = new GuestDto(newPhoneNumber, null, 0, true);

        when(referralTokenRepository.findById(token)).thenReturn(Optional.of(referralToken));

        when(jwtUtils.validateJwtToken(referralToken.getToken())).thenReturn(true);
        when(jwtUtils.getReferralRequestFromJwtToken(referralToken.getToken())).thenReturn(referralRequest);

        when(metadataRepository.findById(1l)).thenReturn(Optional.ofNullable(Metadata.builder().pointsPerReferral(10).build()));

        when(guestRepository.findById(referralRequest.getNewGuestPhone())).thenReturn(Optional.empty());
        when(guestRepository.findById(referralRequest.getCurrentGuestPhone())).thenReturn(Optional.of(existingGuest));
        when(guestMapper.toDto(any(Guest.class))).thenReturn(newGuestDto);

        GuestDto guestDto = guestService.acceptReferral(acceptReferralRequest);

        verify(referralTokenRepository).save(referralToken);
        verify(guestRepository, times(2)).save(any(Guest.class));
        assertThat(guestDto).isEqualTo(newGuestDto);
    }

    @Test
    void testAcceptReferralWhenTokenNotValid() {
        String token = "token123";
        String newPhoneNumber = "123";
        String currentPhoneNumber = "456";
        AcceptReferralRequest acceptReferralRequest = new AcceptReferralRequest(token, "789");
        ReferralRequest referralRequest = new ReferralRequest(newPhoneNumber, currentPhoneNumber, false);
        ReferralToken referralToken = new ReferralToken(token, true);
        Guest existingGuest = new Guest(currentPhoneNumber, "mail@mail.com", 20, false);
        GuestDto newGuestDto = new GuestDto(newPhoneNumber, null, 0, true);

        when(referralTokenRepository.findById(token)).thenReturn(Optional.of(referralToken));

        when(jwtUtils.validateJwtToken(referralToken.getToken())).thenReturn(true);
        when(jwtUtils.getReferralRequestFromJwtToken(referralToken.getToken())).thenReturn(referralRequest);

        assertThrows(ReferralTokenNotFoundException.class, () -> guestService.acceptReferral(acceptReferralRequest));
    }

    @Test
    void testAcceptReferralWhenPhoneNotValid() {
        String token = "token123";
        String newPhoneNumber = "123";
        AcceptReferralRequest acceptReferralRequest = new AcceptReferralRequest(token, newPhoneNumber);
        ReferralToken referralToken = new ReferralToken(token, false);

        when(referralTokenRepository.findById(token)).thenReturn(Optional.of(referralToken));

        when(jwtUtils.validateJwtToken(referralToken.getToken())).thenReturn(false);

        assertThrows(ReferralTokenExpiredException.class, () -> guestService.acceptReferral(acceptReferralRequest));
    }
    @Test
    void testAcceptReferralWhenPhoneNotFound() {
        String token = "token123";
        String newPhoneNumber = "123";
        AcceptReferralRequest acceptReferralRequest = new AcceptReferralRequest(token, newPhoneNumber);

        when(referralTokenRepository.findById(token)).thenReturn(Optional.empty());

        assertThrows(ReferralTokenNotFoundException.class, () -> guestService.acceptReferral(acceptReferralRequest));
    }


    @Test
    void testUpdateGustInfo() {
        GuestInfoRequest request = new GuestInfoRequest("123", "new@g.com");

        Guest existingGuest = Guest.builder().phoneNumber(request.getPhoneNumber()).email("old@g.com").build();

        when(guestRepository.findById(request.getPhoneNumber())).thenReturn(Optional.ofNullable(existingGuest));
        when(guestRepository.findByEmail(request.getEmail())).thenReturn(Optional.empty());

        guestService.updateGustInfo(request);

        verify(guestRepository, times(1)).save(any(Guest.class));
    }

    @Test
    void testUpdateGustInfoDuplicateEmail() {
        GuestInfoRequest request = new GuestInfoRequest("123", "new@g.com");
        Guest guest = Guest.builder().phoneNumber(request.getPhoneNumber()).email(null).build();

        Guest existingGuest = Guest.builder().phoneNumber("456").email("new@g.com").build();

        when(guestRepository.findById(request.getPhoneNumber())).thenReturn(Optional.of(guest));
        when(guestRepository.findByEmail(request.getEmail())).thenReturn(Optional.ofNullable(existingGuest));

        assertThrows(DuplicateGuestEmailException.class, () -> guestService.updateGustInfo(request));
    }

    @Test
    void userInfoWithPhone() {
        String phone = "123";
        Guest existingGuest = Guest.builder().phoneNumber(phone).email(null).build();
        when(guestRepository.findById(phone)).thenReturn(Optional.of(existingGuest));

        guestService.userInfoWithPhone(phone);

        verify(guestRepository, times(1)).findById(phone);
    }

    @Test
    void userInfoWithPhoneNotExists() {
        String phone = "123";
        when(guestRepository.findById(phone)).thenReturn(Optional.empty());

        assertThrows(GuestNotFoundException.class, () -> guestService.userInfoWithPhone(phone));
    }

    @Test
    void userInfoWithEmail() {
        String email = "a@a.com";
        Guest existingGuest = Guest.builder().phoneNumber("123").email(email).build();
        when(guestRepository.findByEmail(email)).thenReturn(Optional.of(existingGuest));

        guestService.userInfoWithEmail(email);

        verify(guestRepository, times(1)).findByEmail(email);
    }

    @Test
    void userInfoWithEmailNotExists() {
        String email = "a@a.com";
        when(guestRepository.findByEmail(email)).thenReturn(Optional.empty());
        assertThrows(GuestNotFoundException.class, () -> guestService.userInfoWithEmail(email));
    }

    @Test
    void testGetQuizMetadata() {
        Metadata metadata = Metadata.builder().id(1l).build();

        when(metadataRepository.findById(1l)).thenReturn(Optional.ofNullable(metadata));

        guestService.getQuizMetadata();

        verify(metadataRepository, times(1)).findById(1l);
    }
}