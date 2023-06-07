package com.mehisen.referralquizbackend.controllers;

import com.mehisen.referralquizbackend.models.Redeem;
import com.mehisen.referralquizbackend.payload.dto.RedeemDto;
import com.mehisen.referralquizbackend.payload.request.OTPRedeemRequest;
import com.mehisen.referralquizbackend.payload.request.SubmitRedeemRequest;
import com.mehisen.referralquizbackend.payload.resonse.MessageResponse;
import com.mehisen.referralquizbackend.services.RedeemService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/redeem")
@RequiredArgsConstructor
@CrossOrigin(origins = "http://localhost:4200/")
public class RedeemController {
    private final RedeemService redeemService;

    @GetMapping("/allRedeems")
    public ResponseEntity<List<RedeemDto>> allRedeems() {
        List<RedeemDto> redeemDtos = redeemService.allRedeem();
        return ResponseEntity.ok(redeemDtos);
    }

    @PostMapping("/requestRedeem")
    private ResponseEntity<MessageResponse> submitRedeem(@Valid @RequestBody SubmitRedeemRequest submitRedeemRequest) {
        redeemService.requestRedeem(submitRedeemRequest);
        return ResponseEntity.ok(new MessageResponse("OTP sent successfully"));
    }

    @PostMapping("/submitOTPRedeem")
    private ResponseEntity<MessageResponse> submitRedeem(@Valid @RequestBody OTPRedeemRequest otpRedeemRequest) {
        redeemService.submitToken(otpRedeemRequest);
        return ResponseEntity.ok(new MessageResponse("Submitted Successfully"));
    }
}
