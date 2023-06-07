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
import com.mehisen.referralquizbackend.payload.mapper.RedeemMapper;
import com.mehisen.referralquizbackend.payload.mapper.RedeemHistoryMapper;
import com.mehisen.referralquizbackend.payload.request.OTPRedeemRequest;
import com.mehisen.referralquizbackend.payload.request.SubmitRedeemRequest;
import com.mehisen.referralquizbackend.repositories.GuestRepository;
import com.mehisen.referralquizbackend.repositories.OtpRedeemRepository;
import com.mehisen.referralquizbackend.repositories.RedeemRepository;
import com.mehisen.referralquizbackend.repositories.RedeemHistoryRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import java.security.SecureRandom;
import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Slf4j
public class RedeemService {

    private final RedeemHistoryRepository redeemHistoryRepository;

    private final GuestRepository guestRepository;

    private final RedeemRepository redeemRepository;

    private final RedeemHistoryMapper redeemHistoryMapper;
    private final RedeemMapper redeemMapper;

    private final OtpRedeemRepository otpRedeemRepository;

    private final OTPService otpService;

    public List<RedeemDto> allRedeem() {
        List<Redeem> redeems = redeemRepository.findAllByIsAvailable(true);

        return redeems.stream().map(redeemMapper::toDto).collect(Collectors.toList());
    }

    public void requestRedeem(SubmitRedeemRequest submitRedeemRequest) {
        Redeem redeem = getRedeemById(submitRedeemRequest.getRedeemId());
        Guest guest = getGuestByPhone(submitRedeemRequest.getGuestPhone());

        checkGuestPreviousRequest(guest);

        checkRedeemValidation(guest, redeem);

        sendOtpToken(guest, redeem);
    }

    public void submitToken(OTPRedeemRequest otpRedeemRequest) {
        Guest guest = getGuestByPhone(otpRedeemRequest.getPhoneNumber());
        OTPRedeem otpRedeem = getRedeemByToken(otpRedeemRequest.getToken(), guest);

        submitRedeem(otpRedeem.getGuest(), otpRedeem.getRedeem());
        otpRedeemRepository.delete(otpRedeem);
    }

    private void sendOtpToken(Guest guest, Redeem redeem) {
        String otpToken = generateRandomToken(6);
        LocalDateTime expiryDate = LocalDateTime.now().plus(1, ChronoUnit.MINUTES);
        log.info("OTP: {}", otpToken);

        OTPRedeem otpRedeem = OTPRedeem.builder()
                .otpToken(otpToken)
                .expiryDate(expiryDate)
                .guest(guest)
                .redeem(redeem)
                .build();

        otpRedeemRepository.save(otpRedeem);

        otpService.sendOtpUsingSms(guest.getPhoneNumber(), otpToken);
    }
    private OTPRedeem getRedeemByToken(String token, Guest guest) {
        Optional<OTPRedeem> otpRedeem = otpRedeemRepository.findByOtpTokenAndGuest(token, guest);
        if (otpRedeem.isEmpty()) {
            throw new InvalidOTPRedeemTokenException("Invalid OTP Token");
        }
        return otpRedeem.get();
    }
    private void checkGuestPreviousRequest(Guest guest) {
        Optional<OTPRedeem> otpRedeem = otpRedeemRepository.findByGuest(guest);
        if (otpRedeem.isPresent()) {
            // to remove if cron job not deleted it and guest want to refresh otp
            if (otpRedeem.get().getExpiryDate().isBefore(LocalDateTime.now().minusMinutes(1))) {
                otpRedeemRepository.delete(otpRedeem.get());
            } else {
                throw new InvalidRedeemRequestException("Guest already have current redeem to confirm or waits 1 min");
            }
        }
    }
    private void submitRedeem(Guest guest, Redeem redeem) {
        guest.setTotalPoints(guest.getTotalPoints() - redeem.getPointsToRedeem());
        guestRepository.save(guest);

        RedeemHistory redeemHistory = RedeemHistory.builder()
                .redeem(redeem)
                .guest(guest)
                .pointsForRedeem(redeem.getPointsToRedeem())
                .build();

        redeemHistoryRepository.save(redeemHistory);
    }

    private void checkRedeemValidation(Guest guest, Redeem redeem) {
        if (guest.getTotalPoints() < redeem.getPointsToRedeem()) {
            throw new InvalidRedeemRequestException("Guest points less than redeem point");
        }

        if (!redeem.isAvailable()) {
            throw new InvalidRedeemRequestException("Redeem not available now");
        }
    }


    private Redeem getRedeemById(Long id) {
        Optional<Redeem> redeemOptional = redeemRepository.findById(id);
        if (redeemOptional.isPresent()) {
            return redeemOptional.get();
        } else {
            throw new RedeemNotFoundException("No redeem found with id=" + id);
        }
    }

    private Guest getGuestByPhone(String phone) {
        Optional<Guest> guestOptional = guestRepository.findById(phone);
        if (guestOptional.isEmpty()) {
            throw new GuestNotFoundException("No Guest found with phone =" + phone);
        }
        return guestOptional.get();
    }

    private String generateRandomToken(Integer otpLength) {
        String numbers = "0123456789";
        SecureRandom random = new SecureRandom();
        StringBuilder token = new StringBuilder(otpLength);
        for (int i = 0; i < otpLength; i++) {
            int randomIndex = random.nextInt(numbers.length());
            token.append(numbers.charAt(randomIndex));
        }
        return token.toString();
    }

    @Scheduled(cron = "0 */1 * * * *") // Runs every 1 minute
    public void cleanupExpiredTokens() {
        LocalDateTime expiryDate = LocalDateTime.now().minusMinutes(1);
        otpRedeemRepository.deleteByExpiryDateBefore(expiryDate);
        log.info("OTP Tokens Deleted at {}", LocalDateTime.now());
    }
}
