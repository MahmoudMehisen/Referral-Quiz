package com.mehisen.referralquizbackend.controllers;

import com.mehisen.referralquizbackend.models.ReferralToken;
import com.mehisen.referralquizbackend.payload.dto.GuestDto;
import com.mehisen.referralquizbackend.payload.dto.QuestionDto;
import com.mehisen.referralquizbackend.payload.dto.ReferralDto;
import com.mehisen.referralquizbackend.payload.request.AnswerRequest;
import com.mehisen.referralquizbackend.payload.request.ReferralRequest;
import com.mehisen.referralquizbackend.services.QuizService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/quiz")
@RequiredArgsConstructor
@CrossOrigin(origins = "http://localhost:4200/")
public class QuizController {

    private final QuizService quizService;

    @PostMapping("/startQuiz/{phoneNumber}")
    public ResponseEntity<List<QuestionDto>> startQuiz(@PathVariable String phoneNumber) {
        List<QuestionDto> questions = quizService.startQuiz(phoneNumber);
        return ResponseEntity.ok(questions);
    }

    @PostMapping("/submitQuiz")
    public ResponseEntity<GuestDto> startQuiz(@Valid @RequestBody AnswerRequest answerRequest) {
        GuestDto guestDto = quizService.submitAnswer(answerRequest);
        return ResponseEntity.ok(guestDto);
    }

    @PostMapping("/referralAnotherUser")
    public ResponseEntity<ReferralDto> referralAnotherUser(@Valid @RequestBody ReferralRequest referralRequest) {
        ReferralDto referralToken = quizService.referralAnotherUser(referralRequest);
        return ResponseEntity.ok(referralToken);
    }
}
