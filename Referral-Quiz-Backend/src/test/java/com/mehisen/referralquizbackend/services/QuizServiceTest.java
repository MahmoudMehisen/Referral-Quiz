package com.mehisen.referralquizbackend.services;

import com.mehisen.referralquizbackend.exception.GuestCanNotDoReferralException;
import com.mehisen.referralquizbackend.exception.GuestNotFoundException;
import com.mehisen.referralquizbackend.exception.RedeemNotFoundException;
import com.mehisen.referralquizbackend.models.*;
import com.mehisen.referralquizbackend.payload.dto.GuestDto;
import com.mehisen.referralquizbackend.payload.dto.QuestionGroupDto;
import com.mehisen.referralquizbackend.payload.mapper.GuestMapper;
import com.mehisen.referralquizbackend.payload.mapper.QuestionGroupMapper;
import com.mehisen.referralquizbackend.payload.mapper.ReferralMapper;
import com.mehisen.referralquizbackend.payload.request.AnswerQuestionOption;
import com.mehisen.referralquizbackend.payload.request.AnswerRequest;
import com.mehisen.referralquizbackend.payload.request.ReferralRequest;
import com.mehisen.referralquizbackend.repositories.GuestRepository;
import com.mehisen.referralquizbackend.repositories.MetadataRepository;
import com.mehisen.referralquizbackend.repositories.QuestionGroupsRepository;
import com.mehisen.referralquizbackend.repositories.ReferralTokenRepository;
import com.mehisen.referralquizbackend.security.jwt.JwtUtils;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.test.context.ActiveProfiles;

import java.lang.reflect.Array;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
@Slf4j
@ActiveProfiles("test")
class QuizServiceTest {
    @Mock
    private GuestRepository guestRepository;
    @Mock
    private MetadataRepository metadataRepository;
    @Mock
    private QuestionGroupsRepository questionGroupsRepository;
    @Mock
    private JwtUtils jwtUtils;
    @Mock
    private ReferralTokenRepository referralTokenRepository;
    @Mock
    private QuestionGroupMapper questionGroupMapper;
    @Mock
    private ReferralMapper referralMapper;
    @Mock
    private GuestMapper guestMapper;

    @InjectMocks
    private QuizService quizService;

    @Test
    public void testStartQuiz() {
        String phoneNumber = "123";
        Guest guest = Guest.builder().phoneNumber(phoneNumber).canPlay(true).build();
        Metadata metadata = Metadata.builder().activeGroupId(1l).build();
        QuestionGroup activeGroup = QuestionGroup.builder().id(1l).questions(List.of()).build();

        when(guestRepository.findById(phoneNumber)).thenReturn(Optional.ofNullable(guest));
        when(metadataRepository.findById(1l)).thenReturn(Optional.ofNullable(metadata));
        when(questionGroupsRepository.findById(activeGroup.getId())).thenReturn(Optional.of(activeGroup));
        when(questionGroupMapper.toDto(any(QuestionGroup.class), eq(false))).thenReturn(new QuestionGroupDto());

        quizService.startQuiz(phoneNumber);

        verify(guestRepository, times(1)).save(any(Guest.class));
    }

    @Test
    public void testStartQuizGuestCannotPlay() {
        String phoneNumber = "123";
        Guest guest = Guest.builder().phoneNumber(phoneNumber).canPlay(false).build();

        when(guestRepository.findById(phoneNumber)).thenReturn(Optional.ofNullable(guest));

        assertThrows(GuestNotFoundException.class, () -> quizService.startQuiz(phoneNumber));
    }

    @Test
    public void testStartQuizGuestNotFound() {
        String phoneNumber = "123";

        when(guestRepository.findById(phoneNumber)).thenReturn(Optional.empty());

        assertThrows(GuestNotFoundException.class, () -> quizService.startQuiz(phoneNumber));
    }

    @Test
    public void testSubmitQuiz() {
        String phoneNumber = "123";
        Guest guest = Guest.builder().phoneNumber(phoneNumber).canPlay(true).totalPoints(0).build();
        QuestionGroup activeGroup = QuestionGroup.builder().id(1l)
                .questions(List.of(Question.builder().id(1l)
                        .options(List.of(QuestionOption.builder().id(1l).selectedTimes(0).isCorrectAnswer(true).build()))
                        .build()))
                .numberOfPlayed(0).build();
        Metadata metadata = Metadata.builder().activeGroupId(1l).pointsPerQuestion(10).build();
        AnswerRequest answerRequest = new AnswerRequest();
        answerRequest.setPhoneNumber(phoneNumber);
        answerRequest.setAnswerQuestionOptionList(List.of(
                new AnswerQuestionOption(1l, 1l)
        ));

        when(guestRepository.findById(phoneNumber)).thenReturn(Optional.of(guest));
        when(metadataRepository.findById(1l)).thenReturn(Optional.of(metadata));
        when(questionGroupsRepository.findById(activeGroup.getId())).thenReturn(Optional.of(activeGroup));
        when(guestMapper.toDto(any(Guest.class))).thenReturn(new GuestDto());

        GuestDto result = quizService.submitAnswer(answerRequest);

        assertNotNull(result);

        assertEquals(1, activeGroup.getNumberOfPlayed());
        assertEquals(10, guest.getTotalPoints());

        verify(questionGroupsRepository, times(1)).save(activeGroup);
        verify(guestRepository, times(1)).save(guest);
    }

    @Test
    public void testReferralAnotherUser() {
        ReferralRequest referralRequest = new ReferralRequest("123", "456", false);
        Metadata metadata = Metadata.builder().canUserDoReferral(true).referralExpirationTime(3000).build();
        Guest guest = Guest.builder().phoneNumber("456").build();

        when(metadataRepository.findById(1l)).thenReturn(Optional.of(metadata));
        when(jwtUtils.generateJwtTokenForReferral(any(ReferralRequest.class), anyInt())).thenReturn("token123");
        when(guestRepository.findById("456")).thenReturn(Optional.of(guest));

        quizService.referralAnotherUser(referralRequest);

        verify(referralTokenRepository, times(1)).save(any(ReferralToken.class));
    }

    @Test
    public void testReferralAnotherUserAdminCloseReferral() {
        ReferralRequest referralRequest = new ReferralRequest("123", "456", false);
        Metadata metadata = Metadata.builder().canUserDoReferral(false).referralExpirationTime(3000).build();

        when(metadataRepository.findById(1l)).thenReturn(Optional.of(metadata));

        assertThrows(GuestCanNotDoReferralException.class, () -> quizService.referralAnotherUser(referralRequest));
    }

}