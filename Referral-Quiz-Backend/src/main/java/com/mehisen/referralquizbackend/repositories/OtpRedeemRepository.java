package com.mehisen.referralquizbackend.repositories;

import com.mehisen.referralquizbackend.models.Guest;
import com.mehisen.referralquizbackend.models.OTPRedeem;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.Optional;

@Repository
public interface OtpRedeemRepository extends JpaRepository<OTPRedeem,Long> {
    Optional<OTPRedeem> findByGuest(Guest guest);
    Optional<OTPRedeem> findByOtpTokenAndGuest(String otpToken,Guest guest);

    @Transactional
    void deleteByExpiryDateBefore(LocalDateTime expiryDate);
}
