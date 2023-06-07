package com.mehisen.referralquizbackend.services;

import com.mehisen.referralquizbackend.exception.GroupNotFoundException;
import com.mehisen.referralquizbackend.exception.GuestNotFoundException;
import com.mehisen.referralquizbackend.exception.QuestionNotFoundException;
import com.mehisen.referralquizbackend.exception.RedeemNotFoundException;
import com.mehisen.referralquizbackend.models.*;
import com.mehisen.referralquizbackend.payload.dto.*;
import com.mehisen.referralquizbackend.payload.mapper.*;
import com.mehisen.referralquizbackend.payload.request.AddQuestionRequest;
import com.mehisen.referralquizbackend.payload.request.AddRedeemRequest;
import com.mehisen.referralquizbackend.payload.request.OptionRequest;
import com.mehisen.referralquizbackend.payload.request.ReferralRequest;
import com.mehisen.referralquizbackend.repositories.*;
import com.mehisen.referralquizbackend.security.jwt.JwtUtils;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.test.context.ActiveProfiles;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
@Slf4j
@ActiveProfiles("test")
public class AdminServiceTest {

    @Mock
    private QuestionRepository questionRepository;

    @Mock
    private MetadataRepository metadataRepository;

    @Mock
    private ReferralTokenRepository referralTokenRepository;

    @Mock
    private JwtUtils jwtUtils;
    @Mock
    private QuestionGroupsRepository questionGroupsRepository;
    @Mock
    private GuestRepository guestRepository;
    @Mock
    private RedeemRepository redeemRepository;
    @Mock
    private RedeemHistoryRepository redeemHistoryRepository;
    @Mock
    private QuestionGroupMapper questionGroupMapper;
    @Mock
    private QuestionMapper questionMapper;
    @Mock
    private MetadataMapper metadataMapper;
    @Mock
    private ReferralMapper referralMapper;
    @Mock
    private GuestMapper guestMapper;
    @Mock
    private RedeemMapper redeemMapper;
    @Mock
    private RedeemHistoryMapper redeemHistoryMapper;
    @InjectMocks
    private AdminService adminService;

    private QuestionGroup getRandomGroup(Long id) {
        return QuestionGroup.builder()
                .id(id)
                .name("Sport")
                .numberOfPlayed(0)
                .build();
    }


    @Test
    void testAddGroup() {
        adminService.addGroup("Sport");
        verify(questionGroupsRepository, times(1)).save(any(QuestionGroup.class));
        verifyNoMoreInteractions(questionGroupsRepository);
    }

    @Test
    void testUpdateGroup() {
        QuestionGroup questionGroup = getRandomGroup(1l);

        when(questionGroupsRepository.findById(1L)).thenReturn(Optional.of(questionGroup));
        adminService.updateGroup(QuestionGroupDto.builder().id(1l).build());

        verify(questionGroupsRepository, times(1)).save(any(QuestionGroup.class));
        verifyNoMoreInteractions(questionGroupsRepository);
    }

    @Test
    void testUpdateGroupWhenGroupNotFound() {
        when(questionGroupsRepository.findById(1L)).thenReturn(Optional.empty());
        assertThrows(GroupNotFoundException.class, () -> adminService.updateGroup(QuestionGroupDto.builder().id(1l).build()));
    }


    @Test
    void testAddQuestion() {
        QuestionGroup questionGroup = getRandomGroup(1l);
        AddQuestionRequest addQuestionRequest = new AddQuestionRequest();
        addQuestionRequest.setQuestionText("What is the capital of France?");
        List<OptionRequest> options = new ArrayList<>();
        options.add(new OptionRequest("Cairo", false));
        options.add(new OptionRequest("Paris", true));
        options.add(new OptionRequest("London", false));
        options.add(new OptionRequest("Chennai", false));
        addQuestionRequest.setQuestionOptions(options);
        addQuestionRequest.setGroupId(1l);

        when(questionGroupsRepository.findById(1L)).thenReturn(Optional.of(questionGroup));

        adminService.addQuestion(addQuestionRequest);


        verify(questionRepository, times(1)).save(any(Question.class));
    }

    @Test
    void testGetAllGroupQuestions() {
        List<QuestionGroup> groups = Arrays.asList(
                getRandomGroup(1l),
                getRandomGroup(2l)
        );

        when(questionGroupsRepository.findAll()).thenReturn(groups);
        //when(questionGroupMapper.toDto(any(QuestionGroup.class),eq(true))).thenReturn(new QuestionGroupDto());

        List<QuestionGroupDto> groupDtos = adminService.allGroupWithQuestions();

        verify(questionGroupsRepository, times(1)).findAll();
        assertEquals(2, groupDtos.size());
    }

    @Test
    public void testGetMetadata() {
        Metadata metadata = Metadata.builder().id(1l).build();
        when(metadataRepository.findById(1l)).thenReturn(Optional.ofNullable(metadata));
        adminService.getMetadata();
        verify(metadataRepository, times(1)).findById(1l);
    }

    @Test
    public void testUpdateQuestionWhenQuestionExists() {
        Question existingQuestion = Question.builder().id(1l).questionText("What is the capital of France?").options(List.of(
                QuestionOption.builder().id(1l).optionText("answer").isCorrectAnswer(true).build()
        )).build();

        QuestionDto newQuestion = QuestionDto.builder().id(1l).questionText("What is the capital of Egypt?").options(List.of(
                QuestionOptionDto.builder().id(1l).optionText("answer2").isCorrectAnswer(false).build()
        )).build();

        when(questionRepository.findById(1L)).thenReturn(Optional.of(existingQuestion));

        adminService.updateQuestion(newQuestion);

        verify(questionRepository, times(1)).save(any(Question.class));
    }

    @Test
    public void testUpdateQuestionWhenQuestionDoesNotExist() {
        QuestionDto newQuestion = QuestionDto.builder().id(1l).questionText("What is the capital of Egypt?").options(List.of()).build();

        when(questionRepository.findById(anyLong())).thenReturn(Optional.empty());

        assertThrows(QuestionNotFoundException.class, () -> adminService.updateQuestion(newQuestion));
    }

    @Test
    public void testUpdateMetadata() {
        Metadata metadata = Metadata.builder().id(1l).build();

        when(metadataMapper.fromDto(any(MetadataDto.class))).thenReturn(metadata);


        adminService.updateMetadata(MetadataDto.builder().id(1l).build());
        verify(metadataRepository, times(1)).save(any(Metadata.class));
    }

    @Test
    public void testDeleteQuestionWhenQuestionExists() {
        Long id = 1L;
        Question question = new Question();
        question.setId(id);
        when(questionRepository.findById(id)).thenReturn(Optional.of(question));

        //When
        adminService.deleteQuestion(id);

        //Then
        verify(questionRepository, times(1)).delete(question);
    }

    @Test
    public void testDeleteQuestionWhenQuestionDoesNotExist() {
        Long id = 1L;

        when(questionRepository.findById(id)).thenReturn(Optional.empty());

        assertThrows(QuestionNotFoundException.class, () -> adminService.deleteQuestion(id));

        verify(questionRepository, never()).delete(any());
    }


    @Test
    public void testGenerateReferral() {
        Metadata metadata = new Metadata();
        metadata.setReferralExpirationTime(30);

        when(metadataRepository.findById(1l)).thenReturn(Optional.of(metadata));
        when(jwtUtils.generateJwtTokenForReferral(any(ReferralRequest.class), any(Integer.class))).thenReturn("token123");
        when(referralMapper.toDto(any(ReferralToken.class))).thenReturn(new ReferralDto("token123"));

        ReferralDto referralToken = adminService.generateReferral("011");

        assertNotNull(referralToken.getToken());
        verify(referralTokenRepository, times(1)).save(any(ReferralToken.class));
    }

    @Test
    public void testActiveGroup() {
        Metadata metadata = Metadata.builder().id(1l).build();

        when(metadataRepository.findById(1l)).thenReturn(Optional.ofNullable(metadata));
        adminService.activeGroup(2l);
        verify(metadataRepository, times(1)).save(metadata);
    }

    @Test
    public void testStopQuiz() {
        Metadata metadata = Metadata.builder().id(1l).activeGroupId(2l).build();

        when(metadataRepository.findById(1l)).thenReturn(Optional.ofNullable(metadata));
        adminService.stopQuiz();
        verify(metadataRepository, times(1)).save(metadata);
    }

    @Test
    public void testAllGuests() {
        List<Guest> guests = Arrays.asList(
                Guest.builder().phoneNumber("1").build(),
                Guest.builder().phoneNumber("2").build()
        );
        when(guestRepository.findAll()).thenReturn(guests);
        //when(questionGroupMapper.toDto(any(QuestionGroup.class),eq(true))).thenReturn(new QuestionGroupDto());

        List<GuestDto> guestDtos = adminService.allGuests();

        verify(guestRepository, times(1)).findAll();
        assertEquals(2, guestDtos.size());
    }

    @Test
    public void testGuestEnablePlay() {
        Guest guest = Guest.builder().phoneNumber("011").build();

        when(guestRepository.findById("011")).thenReturn(Optional.ofNullable(guest));

        adminService.guestEnablePlay("011");

        verify(guestRepository, times(1)).save(guest);
    }

    @Test
    public void testGuestDisablePlay() {
        Guest guest = Guest.builder().phoneNumber("011").build();

        when(guestRepository.findById("011")).thenReturn(Optional.ofNullable(guest));

        adminService.guestDisablePlay("011");

        verify(guestRepository, times(1)).save(guest);
    }

    @Test
    public void testGuestEnablePlayGuestNotFound() {
        when(guestRepository.findById("011")).thenReturn(Optional.empty());

        assertThrows(GuestNotFoundException.class, () -> adminService.guestEnablePlay("011"));
    }

    @Test
    public void testAddNewRedeem() {
        AddRedeemRequest addRedeemRequest = new AddRedeemRequest("chair", 100);
        adminService.addNewRedeem(addRedeemRequest);
        verify(redeemRepository, times(1)).save(any(Redeem.class));
        verifyNoMoreInteractions(questionGroupsRepository);
    }

    @Test
    public void testUpdateRedeem() {
        RedeemDto redeemDto = RedeemDto.builder()
                .id(1l).redeemName("chair")
                .pointsToRedeem(100)
                .isAvailable(true)
                .build();

        Redeem redeem = Redeem.builder().id(1l).build();

        when(redeemRepository.findById(1l)).thenReturn(Optional.ofNullable(redeem));

        adminService.updateRedeem(redeemDto);

        verify(redeemRepository, times(1)).save(any(Redeem.class));
    }

    @Test
    public void testUpdateRedeemNotFound() {
        RedeemDto redeemDto = RedeemDto.builder().id(1l).build();

        when(redeemRepository.findById(1l)).thenReturn(Optional.empty());

        assertThrows(RedeemNotFoundException.class, () -> adminService.updateRedeem(redeemDto));
    }

    @Test
    public void testAllRedeems() {
        List<Redeem> redeems = Arrays.asList(
                Redeem.builder().id(1l).build(),
                Redeem.builder().id(2l).build()
        );
        when(redeemRepository.findAll()).thenReturn(redeems);

        List<RedeemDto> redeemDtos = adminService.allRedeems();

        verify(redeemRepository, times(1)).findAll();
        assertEquals(2, redeemDtos.size());
    }

    @Test
    public void testAllRedeemsHistory() {
        List<RedeemHistory> redeemHistories = Arrays.asList(
                RedeemHistory.builder().id(1l).build(),
                RedeemHistory.builder().id(2l).build()
        );
        when(redeemHistoryRepository.findAll()).thenReturn(redeemHistories);

        List<RedeemHistoryDto> redeemHistoryDtos = adminService.allRedeemHistory();

        verify(redeemHistoryRepository, times(1)).findAll();
        assertEquals(2, redeemHistoryDtos.size());
    }
}
