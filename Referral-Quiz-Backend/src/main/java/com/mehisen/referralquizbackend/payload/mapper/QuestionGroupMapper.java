package com.mehisen.referralquizbackend.payload.mapper;

import com.mehisen.referralquizbackend.models.QuestionGroup;
import com.mehisen.referralquizbackend.payload.dto.QuestionGroupDto;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;

import java.util.stream.Collectors;

@Component
@RequiredArgsConstructor
public class QuestionGroupMapper {

    private final QuestionMapper questionMapper;

    public  QuestionGroupDto toDtoWithoutQuestions(QuestionGroup questionGroup) {
        return QuestionGroupDto.builder()
                .id(questionGroup.getId())
                .name(questionGroup.getName())
                .numberOfPlayed(questionGroup.getNumberOfPlayed())
                .build();
    }

    public  QuestionGroupDto toDto(QuestionGroup questionGroup, Boolean fromAdmin) {
        return QuestionGroupDto.builder()
                .id(questionGroup.getId())
                .name(questionGroup.getName())
                .numberOfPlayed(questionGroup.getNumberOfPlayed())
                .questions(questionGroup.getQuestions().stream()
                        .map(question -> questionMapper.toDto(question, fromAdmin))
                        .collect(Collectors.toList()))
                .build();
    }

    public  QuestionGroup fromDto(QuestionGroupDto questionGroupDto) {
        return QuestionGroup.builder()
                .id(questionGroupDto.getId())
                .name(questionGroupDto.getName())
                .questions(questionGroupDto.getQuestions().stream()
                        .map(questionMapper::fromDto)
                        .collect(Collectors.toList()))
                .build();
    }
}
