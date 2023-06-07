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
import com.mehisen.referralquizbackend.repositories.ReferralTokenRepository;
import com.mehisen.referralquizbackend.security.jwt.JwtUtils;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@Slf4j
@RequiredArgsConstructor
public class GuestService {
    private final JwtUtils jwtUtils;

    private final ReferralTokenRepository referralTokenRepository;

    private final GuestRepository guestRepository;

    private final MetadataRepository metadataRepository;

    private final GuestMapper guestMapper;

    private final MetadataMapper metadataMapper;

    public GuestDto acceptReferral(AcceptReferralRequest acceptReferralRequest) {
        ReferralToken referralToken = getReferralTokenById(acceptReferralRequest.getToken());

        boolean isValid = jwtUtils.validateJwtToken(referralToken.getToken());

        if (isValid && referralToken.isActive()) {

            ReferralRequest referralRequest = jwtUtils.getReferralRequestFromJwtToken(referralToken.getToken());
            if (referralRequest.getNewGuestPhone().equals(acceptReferralRequest.getPhoneNumber())) {

                Optional<Guest> guestOptional = guestRepository.findById(referralRequest.getNewGuestPhone());
                Guest newGuest;
                if (guestOptional.isEmpty()) {
                    newGuest = Guest.builder().phoneNumber(referralRequest.getNewGuestPhone()).canPlay(true).totalPoints(0).build();
                    if (!referralRequest.getFromAdmin()) {
                        addPointsToReferralGuest(referralRequest.getCurrentGuestPhone());
                    }
                } else {
                    newGuest = guestOptional.get();
                    if (referralRequest.getFromAdmin()) {
                        newGuest.setCanPlay(true);
                    }
                }

                guestRepository.save(newGuest);

                referralToken.setActive(false);
                referralTokenRepository.save(referralToken);
                return guestMapper.toDto(newGuest);
            } else {
                throw new ReferralTokenNotFoundException("Referral Token not found with phone=" + acceptReferralRequest.getPhoneNumber());
            }
        } else {
            referralToken.setActive(false);
            referralTokenRepository.save(referralToken);
            throw new ReferralTokenExpiredException("Referral Token has expired");
        }
    }


    public GuestDto updateGustInfo(GuestInfoRequest guestInfoRequest) {
        Guest guest = getGuestByPhone(guestInfoRequest.getPhoneNumber());

        Optional<Guest> existingGuestWithEmail = guestRepository.findByEmail(guestInfoRequest.getEmail());
        if (existingGuestWithEmail.isPresent() && !existingGuestWithEmail.get().getPhoneNumber().equals(guest.getPhoneNumber())) {
            throw new DuplicateGuestEmailException("Email already exists for another guest");
        }

        guest.setEmail(guestInfoRequest.getEmail());
        guestRepository.save(guest);
        return guestMapper.toDto(guest);
    }

    public GuestDto userInfoWithPhone(String phoneNumber) {
        Guest guest = getGuestByPhone(phoneNumber);
        return guestMapper.toDto(guest);
    }

    public GuestDto userInfoWithEmail(String email) {
        Guest guest = getGuestByEmail(email);
        return guestMapper.toDto(guest);
    }

    public MetadataDto getQuizMetadata() {
        Metadata metadata = metadataRepository.findById(1l).get();
        return metadataMapper.toDto(metadata);
    }

    private void addPointsToReferralGuest(String guestPhoneNumber) {
        Metadata metadata = metadataRepository.findById(1l).get();
        Optional<Guest> referralGuestOptional = guestRepository.findById(guestPhoneNumber);
        if (referralGuestOptional.isPresent()) {
            Guest guest = referralGuestOptional.get();
            guest.setTotalPoints(guest.getTotalPoints() + metadata.getPointsPerReferral());
            guestRepository.save(guest);
        }
    }

    private ReferralToken getReferralTokenById(String token) {
        Optional<ReferralToken> referralTokenOptional = referralTokenRepository.findById(token);
        if (referralTokenOptional.isPresent()) {
            return referralTokenOptional.get();
        } else {
            throw new ReferralTokenNotFoundException("Referral Token Not Found");
        }
    }

    private Guest getGuestByPhone(String phone) {
        Optional<Guest> guestOptional = guestRepository.findById(phone);
        if (guestOptional.isPresent()) {
            return guestOptional.get();
        } else {
            throw new GuestNotFoundException("Guest Not Found with phone=" + phone);
        }
    }

    private Guest getGuestByEmail(String email) {
        Optional<Guest> guestOptional = guestRepository.findByEmail(email);
        if (guestOptional.isPresent()) {
            return guestOptional.get();
        } else {
            throw new GuestNotFoundException("No guest found with email:" + email);
        }
    }


}
