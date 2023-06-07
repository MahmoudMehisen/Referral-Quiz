package com.mehisen.referralquizbackend.services;

import com.infobip.ApiException;
import com.mehisen.referralquizbackend.exception.GuestNotFoundException;
import com.mehisen.referralquizbackend.exception.InvalidOTPRedeemTokenException;
import com.mehisen.referralquizbackend.exception.InvalidRedeemRequestException;
import com.mehisen.referralquizbackend.exception.RedeemNotFoundException;
import com.mehisen.referralquizbackend.models.Guest;
import com.mehisen.referralquizbackend.models.OTPRedeem;
import com.mehisen.referralquizbackend.models.Redeem;
import com.mehisen.referralquizbackend.models.RedeemHistory;
import com.mehisen.referralquizbackend.payload.dto.RedeemDto;
import com.mehisen.referralquizbackend.payload.mapper.RedeemHistoryMapper;
import com.mehisen.referralquizbackend.payload.mapper.RedeemMapper;
import com.mehisen.referralquizbackend.payload.request.OTPRedeemRequest;
import com.mehisen.referralquizbackend.payload.request.SubmitRedeemRequest;
import com.mehisen.referralquizbackend.repositories.GuestRepository;
import com.mehisen.referralquizbackend.repositories.OtpRedeemRepository;
import com.mehisen.referralquizbackend.repositories.RedeemHistoryRepository;
import com.mehisen.referralquizbackend.repositories.RedeemRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class RedeemServiceTest {
    @Mock
    private RedeemHistoryRepository redeemHistoryRepository;
    @Mock
    private GuestRepository guestRepository;
    @Mock
    private RedeemRepository redeemRepository;
    @Mock
    private RedeemHistoryMapper redeemHistoryMapper;
    @Mock
    private RedeemMapper redeemMapper;
    @Mock
    private OtpRedeemRepository otpRedeemRepository;

    @Mock
    private OTPService otpService;
    @InjectMocks
    private RedeemService redeemService;

    @Test
    public void testAllRedeem() {
        List<Redeem> redeems = Arrays.asList(
                Redeem.builder().id(1l).build(),
                Redeem.builder().id(2l).build()
        );
        when(redeemRepository.findAllByIsAvailable(true)).thenReturn(redeems);

        List<RedeemDto> redeemDtos = redeemService.allRedeem();

        verify(redeemRepository, times(1)).findAllByIsAvailable(true);
        assertEquals(2, redeemDtos.size());
    }

    @Test
    public void testRequestRedeem() {
        Redeem redeem = Redeem.builder().id(1l).pointsToRedeem(20).isAvailable(true).build();
        Guest guest = Guest.builder().totalPoints(100).phoneNumber("123").build();

        SubmitRedeemRequest submitRedeemRequest = new SubmitRedeemRequest(guest.getPhoneNumber(), redeem.getId());
        when(redeemRepository.findById(1l)).thenReturn(Optional.of(redeem));
        when(guestRepository.findById("123")).thenReturn(Optional.of(guest));

        redeemService.requestRedeem(submitRedeemRequest);

        verify(otpRedeemRepository, times(1)).save(any(OTPRedeem.class));
    }

    @Test
    public void testRequestRedeemGuestNotFound() {
        Redeem redeem = Redeem.builder().id(1l).pointsToRedeem(20).isAvailable(true).build();
        Guest guest = Guest.builder().totalPoints(100).phoneNumber("123").build();
        SubmitRedeemRequest submitRedeemRequest = new SubmitRedeemRequest(guest.getPhoneNumber(), redeem.getId());

        when(redeemRepository.findById(1l)).thenReturn(Optional.of(redeem));
        when(guestRepository.findById("123")).thenReturn(Optional.empty());

        assertThrows(GuestNotFoundException.class, () -> redeemService.requestRedeem(submitRedeemRequest));
    }

    @Test
    public void testRequestRedeemRedeemNotFound() {
        Redeem redeem = Redeem.builder().id(1l).pointsToRedeem(20).isAvailable(true).build();
        Guest guest = Guest.builder().totalPoints(100).phoneNumber("123").build();
        SubmitRedeemRequest submitRedeemRequest = new SubmitRedeemRequest(guest.getPhoneNumber(), redeem.getId());

        when(redeemRepository.findById(1l)).thenReturn(Optional.empty());

        assertThrows(RedeemNotFoundException.class, () -> redeemService.requestRedeem(submitRedeemRequest));
    }

    @Test
    public void testRequestRedeemRedeemGuestHasCurrentRedeemRequest() {
        Redeem redeem = Redeem.builder().id(1l).pointsToRedeem(20).isAvailable(true).build();
        Guest guest = Guest.builder().totalPoints(100).phoneNumber("123").build();
        SubmitRedeemRequest submitRedeemRequest = new SubmitRedeemRequest(guest.getPhoneNumber(), redeem.getId());

        when(redeemRepository.findById(1l)).thenReturn(Optional.of(redeem));
        when(guestRepository.findById("123")).thenReturn(Optional.of(guest));
        when(otpRedeemRepository.findByGuest(guest)).thenReturn(Optional.of(OTPRedeem.builder().expiryDate(LocalDateTime.now()).build()));

        assertThrows(InvalidRedeemRequestException.class, () -> redeemService.requestRedeem(submitRedeemRequest));
    }

    @Test
    public void testSubmitRedeemPointsLessThanRedeemPoint() {
        Redeem redeem = Redeem.builder().id(1l).pointsToRedeem(20).isAvailable(true).build();
        Guest guest = Guest.builder().totalPoints(10).phoneNumber("123").build();

        SubmitRedeemRequest submitRedeemRequest = new SubmitRedeemRequest(guest.getPhoneNumber(), redeem.getId());
        when(redeemRepository.findById(1l)).thenReturn(Optional.of(redeem));
        when(guestRepository.findById("123")).thenReturn(Optional.of(guest));


        assertThrows(InvalidRedeemRequestException.class, () -> redeemService.requestRedeem(submitRedeemRequest));
    }

    @Test
    public void testSubmitRedeemRedeemNotAvailable() throws ApiException {
        Redeem redeem = Redeem.builder().id(1l).pointsToRedeem(20).isAvailable(false).build();
        Guest guest = Guest.builder().totalPoints(30).phoneNumber("123").build();

        SubmitRedeemRequest submitRedeemRequest = new SubmitRedeemRequest(guest.getPhoneNumber(), redeem.getId());
        when(redeemRepository.findById(1l)).thenReturn(Optional.of(redeem));
        when(guestRepository.findById("123")).thenReturn(Optional.of(guest));
        assertThrows(InvalidRedeemRequestException.class, () -> redeemService.requestRedeem(submitRedeemRequest));
    }

    @Test
    public void testSubmitToken() {
        OTPRedeemRequest otpRedeemRequest = new OTPRedeemRequest("123456", "123");
        Guest guest = Guest.builder().phoneNumber("123").totalPoints(0).build();
        Redeem redeem = Redeem.builder().id(1l).redeemName("redeem").pointsToRedeem(10).build();
        OTPRedeem otpRedeem = OTPRedeem.builder().redeem(redeem).guest(guest).build();
        when(guestRepository.findById(otpRedeemRequest.getPhoneNumber())).thenReturn(Optional.of(guest));
        when(otpRedeemRepository.findByOtpTokenAndGuest(otpRedeemRequest.getToken(), guest)).thenReturn(Optional.of(otpRedeem));

        redeemService.submitToken(otpRedeemRequest);

        verify(otpRedeemRepository).delete(otpRedeem);
    }

    @Test
    public void testSubmitTokenOTPRedeemNotFound() {
        OTPRedeemRequest otpRedeemRequest = new OTPRedeemRequest("123456", "123");
        Guest guest = Guest.builder().phoneNumber("123").totalPoints(0).build();
        when(guestRepository.findById(otpRedeemRequest.getPhoneNumber())).thenReturn(Optional.of(guest));
        when(otpRedeemRepository.findByOtpTokenAndGuest(otpRedeemRequest.getToken(), guest)).thenReturn(Optional.empty());
        assertThrows(InvalidOTPRedeemTokenException.class,()->redeemService.submitToken(otpRedeemRequest));
    }
}