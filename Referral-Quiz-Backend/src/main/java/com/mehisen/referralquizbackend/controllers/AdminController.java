package com.mehisen.referralquizbackend.controllers;

import com.mehisen.referralquizbackend.payload.dto.*;
import com.mehisen.referralquizbackend.payload.request.AddGroupRequest;
import com.mehisen.referralquizbackend.payload.request.AddQuestionRequest;
import com.mehisen.referralquizbackend.payload.request.AddRedeemRequest;
import com.mehisen.referralquizbackend.payload.resonse.MessageResponse;
import com.mehisen.referralquizbackend.services.AdminService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/admin")
@RequiredArgsConstructor
@CrossOrigin(origins = "http://localhost:4200/")

public class AdminController {

    private final AdminService adminService;

    @PostMapping("/addGroup")
    public ResponseEntity<QuestionGroupDto> addGroup(@RequestBody @Valid AddGroupRequest addGroupRequest) {
        QuestionGroupDto questionGroupDto = adminService.addGroup(addGroupRequest.getName());
        return ResponseEntity.status(HttpStatus.CREATED).body(questionGroupDto);
    }

    @PostMapping("/updateGroup")
    public ResponseEntity<QuestionGroupDto> updateGroup(@RequestBody @Valid QuestionGroupDto groupDto) {
        QuestionGroupDto questionGroupDto = adminService.updateGroup(groupDto);
        return ResponseEntity.status(HttpStatus.OK).body(questionGroupDto);
    }

    @GetMapping("/allGroupWithQuestions")
    public ResponseEntity<List<QuestionGroupDto>> allGroupWithQuestions() {
        List<QuestionGroupDto> questionGroupDtos = adminService.allGroupWithQuestions();
        return ResponseEntity.status(HttpStatus.OK).body(questionGroupDtos);
    }

    @PostMapping("/addQuestion")
    public ResponseEntity<QuestionDto> addQuestion(@RequestBody @Valid AddQuestionRequest addQuestionRequest) {
        QuestionDto questionDto = adminService.addQuestion(addQuestionRequest);
        return ResponseEntity.status(HttpStatus.CREATED).body(questionDto);
    }

    @PostMapping("/updateQuestion")
    public ResponseEntity<QuestionDto> updateQuestion(@RequestBody @Valid QuestionDto question) {
        QuestionDto questionDto = adminService.updateQuestion(question);
        return ResponseEntity.ok(questionDto);
    }

    @GetMapping("/metadata")
    public ResponseEntity<MetadataDto> getMetadata() {
        MetadataDto metadata = adminService.getMetadata();
        return ResponseEntity.ok(metadata);
    }

    @PostMapping("/updateMetadata")
    public ResponseEntity<?> updateMetadata(@RequestBody @Valid MetadataDto metadata) {
        MetadataDto metadataDto = adminService.updateMetadata(metadata);
        return ResponseEntity.ok(metadataDto);
    }

    @DeleteMapping("/deleteQuestion/{id}")
    public ResponseEntity<?> deleteQuestion(@PathVariable Long id) {
        adminService.deleteQuestion(id);
        return new ResponseEntity<MessageResponse>(new MessageResponse("Deleted Successfully"), HttpStatus.OK);
    }

    @PostMapping("/generateReferral/{phoneNumber}")
    public ResponseEntity<ReferralDto> generateReferral(@PathVariable String phoneNumber) {
        ReferralDto token = adminService.generateReferral(phoneNumber);
        return ResponseEntity.ok(token);
    }

    @PostMapping("/activeGroup/{groupId}")
    public ResponseEntity<MetadataDto> activeGroup(@PathVariable Long groupId) {
        MetadataDto token = adminService.activeGroup(groupId);
        return ResponseEntity.ok(token);
    }

    @PostMapping("/stopQuiz")
    public ResponseEntity<MetadataDto> stopQuiz() {
        MetadataDto token = adminService.stopQuiz();
        return ResponseEntity.ok(token);
    }

    @GetMapping("/allGuests")
    public ResponseEntity<List<GuestDto>> allGuests() {
        List<GuestDto> guests = adminService.allGuests();
        return ResponseEntity.ok(guests);
    }

    @PostMapping("/guestEnablePlay/{phoneNumber}")
    public ResponseEntity<GuestDto> guestEnablePlay(@PathVariable String phoneNumber) {
        GuestDto guest = adminService.guestEnablePlay(phoneNumber);
        return ResponseEntity.ok(guest);
    }

    @PostMapping("/guestDisablePlay/{phoneNumber}")
    public ResponseEntity<GuestDto> guestDisablePlay(@PathVariable String phoneNumber) {
        GuestDto guest = adminService.guestDisablePlay(phoneNumber);
        return ResponseEntity.ok(guest);
    }

    @PostMapping("/addRedeem")
    public ResponseEntity<RedeemDto> addRedeem(@Valid @RequestBody AddRedeemRequest addRedeemRequest) {
        RedeemDto redeemDto = adminService.addNewRedeem(addRedeemRequest);
        return ResponseEntity.ok(redeemDto);
    }

    @PostMapping("/updateRedeem")
    public ResponseEntity<RedeemDto> updateRedeem(@Valid @RequestBody RedeemDto redeemDto) {
        RedeemDto updatedRedeem = adminService.updateRedeem(redeemDto);
        return ResponseEntity.ok(updatedRedeem);
    }

    @GetMapping("/allRedeems")
    public ResponseEntity<List<RedeemDto>> allRedeems() {
        List<RedeemDto> redeemDtos = adminService.allRedeems();
        return ResponseEntity.ok(redeemDtos);
    }

    @GetMapping("/allRedeemHistory")
    public ResponseEntity<List<RedeemHistoryDto>> allRedeemHistory() {
        List<RedeemHistoryDto> redeemHistoryDtos = adminService.allRedeemHistory();
        return ResponseEntity.ok(redeemHistoryDtos);
    }
}
