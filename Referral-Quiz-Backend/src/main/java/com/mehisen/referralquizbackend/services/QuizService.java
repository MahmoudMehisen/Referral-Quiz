package com.mehisen.referralquizbackend.services;

import com.mehisen.referralquizbackend.exception.GroupNotFoundException;
import com.mehisen.referralquizbackend.exception.GuestCanNotDoReferralException;
import com.mehisen.referralquizbackend.exception.GuestNotFoundException;
import com.mehisen.referralquizbackend.exception.NoActiveGroupException;
import com.mehisen.referralquizbackend.models.*;
import com.mehisen.referralquizbackend.payload.dto.GuestDto;
import com.mehisen.referralquizbackend.payload.dto.QuestionOptionDto;
import com.mehisen.referralquizbackend.payload.dto.QuestionDto;
import com.mehisen.referralquizbackend.payload.dto.ReferralDto;
import com.mehisen.referralquizbackend.payload.mapper.GuestMapper;
import com.mehisen.referralquizbackend.payload.mapper.QuestionGroupMapper;
import com.mehisen.referralquizbackend.payload.mapper.ReferralMapper;
import com.mehisen.referralquizbackend.payload.request.AnswerQuestionOption;
import com.mehisen.referralquizbackend.payload.request.AnswerRequest;
import com.mehisen.referralquizbackend.payload.request.ReferralRequest;
import com.mehisen.referralquizbackend.repositories.*;
import com.mehisen.referralquizbackend.security.jwt.JwtUtils;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.stream.Collectors;

@Service
@Slf4j
@RequiredArgsConstructor
public class QuizService {

    private final GuestRepository guestRepository;

    private final MetadataRepository metadataRepository;

    private final QuestionGroupsRepository questionGroupsRepository;

    private final JwtUtils jwtUtils;

    private final ReferralTokenRepository referralTokenRepository;

    private final QuestionGroupMapper questionGroupMapper;

    private final ReferralMapper referralMapper;

    private final GuestMapper guestMapper;

    public List<QuestionDto> startQuiz(String phoneNumber) {

        Guest guest = getGuestByPhone(phoneNumber);

        if (!guest.getCanPlay()) {
            throw new GuestNotFoundException("Guest with Phone number: " + phoneNumber + " can't play this quiz");
        }

        QuestionGroup activeGroup = getActiveGroup();

        guest.setCanPlay(false);
        guestRepository.save(guest);

        return questionGroupMapper.toDto(activeGroup, false).getQuestions();
    }


    public GuestDto submitAnswer(AnswerRequest answerRequest) {
        QuestionGroup activeGroup = getActiveGroup();
        Guest guest = getGuestByPhone(answerRequest.getPhoneNumber());

        int totalScore = getTotalScore(answerRequest.getAnswerQuestionOptionList(), activeGroup.getQuestions());

        activeGroup.setNumberOfPlayed(activeGroup.getNumberOfPlayed() + 1);
        questionGroupsRepository.save(activeGroup);

        guest.setTotalPoints(guest.getTotalPoints() + totalScore);
        guestRepository.save(guest);

        return guestMapper.toDto(guest);
    }

    public ReferralDto referralAnotherUser(ReferralRequest referralRequest) {
        Metadata metadata = metadataRepository.findById(1l).get();
        if (!metadata.isCanUserDoReferral()) {
            throw new GuestCanNotDoReferralException("Admin close guest referral now");
        }
        Guest guest = getGuestByPhone(referralRequest.getCurrentGuestPhone());

        referralRequest.setFromAdmin(false);

        String token = jwtUtils.generateJwtTokenForReferral(referralRequest, metadata.getReferralExpirationTime());
        ReferralToken referralToken = new ReferralToken(token, true);
        referralTokenRepository.save(referralToken);
        return referralMapper.toDto(referralToken);
    }

    private Guest getGuestByPhone(String phone) {
        Optional<Guest> optionalGuest = guestRepository.findById(phone);
        if (optionalGuest.isEmpty()) {
            throw new GuestNotFoundException("No Guest Found with Phone number: " + phone);
        }
        return optionalGuest.get();
    }

    private QuestionGroup getActiveGroup() {
        Metadata metadata = metadataRepository.findById(1l).get();
        if (metadata.getActiveGroupId() == null) {
            throw new NoActiveGroupException("No active Group right now");
        }
        return getQuestionGroupById(metadata.getActiveGroupId());
    }

    private QuestionGroup getQuestionGroupById(Long id) {
        Optional<QuestionGroup> groupOptional = questionGroupsRepository.findById(id);
        if (groupOptional.isEmpty()) {
            throw new GroupNotFoundException("No group found with id " + id);
        }
        return groupOptional.get();
    }

    private int getTotalScore(List<AnswerQuestionOption> answers, List<Question> questions) {
        Metadata metadata = metadataRepository.findById(1l).get();

        Map<Long, Map<Long, QuestionOption>> optionsByQuestionId = new HashMap<>();
        for (Question question : questions) {
            Map<Long, QuestionOption> optionsById = new HashMap<>();
            for (QuestionOption option : question.getOptions()) {
                optionsById.put(option.getId(), option);
            }
            optionsByQuestionId.put(question.getId(), optionsById);
        }

        int totalScore = 0;
        for (AnswerQuestionOption answer : answers) {
            Map<Long, QuestionOption> optionsById = optionsByQuestionId.get(answer.getQuestionId());
            if (optionsById != null) {
                QuestionOption option = optionsById.get(answer.getOptionId());
                if (option != null) {
                    option.setSelectedTimes(option.getSelectedTimes() + 1);
                    if (option.getIsCorrectAnswer()) {
                        totalScore += metadata.getPointsPerQuestion();
                    }
                }
            }
        }

        return totalScore;
    }
}
