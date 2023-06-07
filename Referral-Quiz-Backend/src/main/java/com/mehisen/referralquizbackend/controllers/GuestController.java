package com.mehisen.referralquizbackend.controllers;

import com.mehisen.referralquizbackend.models.Guest;
import com.mehisen.referralquizbackend.payload.dto.GuestDto;
import com.mehisen.referralquizbackend.payload.dto.MetadataDto;
import com.mehisen.referralquizbackend.payload.request.AcceptReferralRequest;
import com.mehisen.referralquizbackend.payload.request.GuestInfoRequest;
import com.mehisen.referralquizbackend.payload.resonse.MessageResponse;
import com.mehisen.referralquizbackend.services.GuestService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/guest")
@RequiredArgsConstructor
@CrossOrigin(origins = "http://localhost:4200/")
public class GuestController {
    private final GuestService guestService;

    @PostMapping("/acceptReferral")
    public ResponseEntity<GuestDto> acceptReferral(@Valid @RequestBody AcceptReferralRequest acceptReferralRequest) {
        GuestDto guest = guestService.acceptReferral(acceptReferralRequest);
        return ResponseEntity.ok(guest);
    }

    @PostMapping("/updateInfo")
    public ResponseEntity<GuestDto> updateGuestInfo(@RequestBody GuestInfoRequest guestInfoRequest) {
        GuestDto guest = guestService.updateGustInfo(guestInfoRequest);
        return ResponseEntity.ok(guest);
    }

    @GetMapping("/getGuestByPhone/{phoneNumber}")
    public ResponseEntity<GuestDto> getGuestByPhoneNumber(@PathVariable String phoneNumber) {
        GuestDto guest = guestService.userInfoWithPhone(phoneNumber);
        return ResponseEntity.ok(guest);
    }

    @GetMapping("/getGuestByEmail/{email}")
    public ResponseEntity<GuestDto> getGuestByEmail(@PathVariable String email) {
        GuestDto guest = guestService.userInfoWithEmail(email);
        return ResponseEntity.ok(guest);
    }

    @GetMapping("/getQuizMetadata")
    public ResponseEntity<MetadataDto> getQuizMetadata() {
        MetadataDto metadata = guestService.getQuizMetadata();
        return ResponseEntity.ok(metadata);
    }
}