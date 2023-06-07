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
import com.mehisen.referralquizbackend.payload.request.ReferralRequest;
import com.mehisen.referralquizbackend.repositories.*;
import com.mehisen.referralquizbackend.security.jwt.JwtUtils;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;


import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@Slf4j
@RequiredArgsConstructor
public class AdminService {

    private final QuestionRepository questionRepository;

    private final MetadataRepository metadataRepository;

    private final JwtUtils jwtUtils;

    private final ReferralTokenRepository referralTokenRepository;

    private final QuestionGroupsRepository questionGroupsRepository;

    private final GuestRepository guestRepository;

    private final RedeemHistoryRepository redeemHistoryRepository;

    private final RedeemRepository redeemRepository;

    private final QuestionGroupMapper questionGroupMapper;

    private final QuestionMapper questionMapper;

    private final MetadataMapper metadataMapper;

    private final ReferralMapper referralMapper;

    private final GuestMapper guestMapper;

    private final RedeemMapper redeemMapper;

    private final RedeemHistoryMapper redeemHistoryMapper;

    public QuestionGroupDto addGroup(String name) {
        QuestionGroup questionGroup = QuestionGroup.builder().name(name).numberOfPlayed(0).build();
        questionGroupsRepository.save(questionGroup);
        return questionGroupMapper.toDtoWithoutQuestions(questionGroup);
    }

    public QuestionGroupDto updateGroup(QuestionGroupDto groupDto) {
        QuestionGroup questionGroup = getQuestionGroupById(groupDto.getId());
        questionGroup.setName(groupDto.getName());
        questionGroupsRepository.save(questionGroup);
        return questionGroupMapper.toDto(questionGroup, true);
    }

    public List<QuestionGroupDto> allGroupWithQuestions() {
        List<QuestionGroup> questionGroups = questionGroupsRepository.findAll();
        return questionGroups.stream()
                .map(questionGroup -> questionGroupMapper.toDto(questionGroup, true))
                .collect(Collectors.toList());
    }

    public QuestionDto addQuestion(AddQuestionRequest addQuestionRequest) {
        QuestionGroup questionGroup = getQuestionGroupById(addQuestionRequest.getGroupId());
        Question question = Question.builder()
                .questionText(addQuestionRequest.getQuestionText())
                .questionGroup(questionGroup)
                .build();
        question.setOptions(addQuestionRequest.getQuestionOptions().stream()
                .map(questionOption -> QuestionOption.builder()
                        .question(question)
                        .optionText(questionOption.getOptionText())
                        .isCorrectAnswer(questionOption.isCorrectAnswer())
                        .selectedTimes(0)
                        .build())
                .collect(Collectors.toList()));
        questionRepository.save(question);
        return questionMapper.toDto(question, true);
    }

    public MetadataDto getMetadata() {
        Metadata metadata = metadataRepository.findById(1l).get();
        return metadataMapper.toDto(metadata);
    }


    public QuestionDto updateQuestion(QuestionDto updatedQuestion) {
        Question question = getQuestionById(updatedQuestion.getId());

        question.setQuestionText(updatedQuestion.getQuestionText());

        List<QuestionOption> options = updateOptionsList(question.getOptions(), updatedQuestion.getOptions());

        question.setOptions(options);

        questionRepository.save(question);

        return questionMapper.toDto(question, true);
    }

    public void deleteQuestion(Long id) {
        Question question = getQuestionById(id);
        questionRepository.delete(question);
    }

    public ReferralDto generateReferral(String phoneNumber) {
        Metadata metadata = metadataRepository.findById(1l).get();

        ReferralRequest referralRequest = new ReferralRequest(phoneNumber, "0", true);

        String token = jwtUtils.generateJwtTokenForReferral(referralRequest, metadata.getReferralExpirationTime());
        ReferralToken referralToken = new ReferralToken(token, true);
        referralTokenRepository.save(referralToken);

        return referralMapper.toDto(referralToken);
    }

    public MetadataDto updateMetadata(MetadataDto newMetadata) {
        Metadata metadata = metadataMapper.fromDto(newMetadata);
        // to avoid to changed
        metadata.setId(1l);
        metadataRepository.save(metadata);
        return metadataMapper.toDto(metadata);
    }

    public MetadataDto activeGroup(Long groupId) {
        Metadata metadata = metadataRepository.findById(1l).get();
        metadata.setActiveGroupId(groupId);
        metadataRepository.save(metadata);
        return metadataMapper.toDto(metadata);
    }

    public MetadataDto stopQuiz() {
        Metadata metadata = metadataRepository.findById(1l).get();
        metadata.setActiveGroupId(null);
        metadataRepository.save(metadata);
        return metadataMapper.toDto(metadata);
    }

    public List<GuestDto> allGuests() {
        List<Guest> guests = guestRepository.findAll();
        return guests.stream().map(guestMapper::toDto).collect(Collectors.toList());
    }

    public GuestDto guestEnablePlay(String phone) {
        Guest guest = getGuestByPhone(phone);
        guest.setCanPlay(true);
        guestRepository.save(guest);
        return guestMapper.toDto(guest);
    }

    public GuestDto guestDisablePlay(String phone) {
        Guest guest = getGuestByPhone(phone);
        guest.setCanPlay(false);
        guestRepository.save(guest);
        return guestMapper.toDto(guest);
    }

    public RedeemDto addNewRedeem(AddRedeemRequest addRedeemRequest) {
        Redeem redeem = Redeem.builder()
                .redeemName(addRedeemRequest.getRedeemName())
                .pointsToRedeem(addRedeemRequest.getPointsForRedeem())
                .isAvailable(true)
                .build();

        redeemRepository.save(redeem);

        return redeemMapper.toDto(redeem);
    }

    public RedeemDto updateRedeem(RedeemDto redeemDto) {
        Redeem redeem = getRedeemById(redeemDto.getId());

        redeem.setPointsToRedeem(redeemDto.getPointsToRedeem());
        redeem.setRedeemName(redeemDto.getRedeemName());
        redeem.setAvailable(redeemDto.isAvailable());
        redeemRepository.save(redeem);

        return redeemMapper.toDto(redeem);
    }

    public List<RedeemDto> allRedeems() {
        List<Redeem> redeems = redeemRepository.findAll();

        return redeems.stream().map(redeemMapper::toDto).collect(Collectors.toList());
    }

    public List<RedeemHistoryDto> allRedeemHistory() {
        List<RedeemHistory> redeemHistories = redeemHistoryRepository.findAll();

        return redeemHistories.stream().map(redeemHistoryMapper::toDto).collect(Collectors.toList());
    }

    private Redeem getRedeemById(Long id) {
        Optional<Redeem> redeemOptional = redeemRepository.findById(id);
        if (redeemOptional.isPresent()) {
            return redeemOptional.get();
        } else {
            throw new RedeemNotFoundException("No redeem found with id=" + id);
        }
    }

    private QuestionGroup getQuestionGroupById(Long id) {
        Optional<QuestionGroup> questionGroupOptional = questionGroupsRepository.findById(id);
        if (questionGroupOptional.isEmpty()) {
            throw new GroupNotFoundException("No group found with id " + id);
        }
        return questionGroupOptional.get();
    }

    private Question getQuestionById(Long id) {
        Optional<Question> question = questionRepository.findById(id);
        if (!question.isPresent()) {
            throw new QuestionNotFoundException("No Question found with id =" + id);
        }
        return question.get();
    }

    private Guest getGuestByPhone(String phone) {
        Optional<Guest> guestOptional = guestRepository.findById(phone);
        if (guestOptional.isEmpty()) {
            throw new GuestNotFoundException("No Guest found with phone =" + phone);
        }
        return guestOptional.get();
    }

    private List<QuestionOption> updateOptionsList(List<QuestionOption> options, List<QuestionOptionDto> optionDtos) {
        for (QuestionOption option : options) {
            for (QuestionOptionDto optionDto : optionDtos) {
                if (option.getId().equals(optionDto.getId())) {
                    option.setOptionText(optionDto.getOptionText());
                    option.setIsCorrectAnswer(optionDto.getIsCorrectAnswer());
                    break;
                }
            }
        }
        return options;
    }
}
