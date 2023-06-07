package com.mehisen.referralquizbackend.payload.mapper;

import com.mehisen.referralquizbackend.models.Question;
import com.mehisen.referralquizbackend.payload.dto.QuestionDto;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.stream.Collectors;

@Component
@RequiredArgsConstructor
public class QuestionMapper {

    private final QuestionOptionMapper questionOptionMapper;

    public QuestionDto toDto(Question question, Boolean fromAdmin) {
        return QuestionDto.builder()
                .id(question.getId())
                .questionText(question.getQuestionText())
                .options(question.getOptions().stream()
                        .map(questionOption -> questionOptionMapper.toDto(questionOption, fromAdmin))
                        .collect(Collectors.toList()))
                .build();
    }

    public Question fromDto(QuestionDto questionDto) {
        return Question.builder()
                .id(questionDto.getId())
                .questionText(questionDto.getQuestionText())
                .options(questionDto.getOptions().stream()
                        .map(questionOptionMapper::fromDto)
                        .collect(Collectors.toList()))
                .build();
    }
}
