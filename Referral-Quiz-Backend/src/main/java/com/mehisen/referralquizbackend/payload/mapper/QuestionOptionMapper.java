package com.mehisen.referralquizbackend.payload.mapper;

import com.mehisen.referralquizbackend.models.QuestionOption;
import com.mehisen.referralquizbackend.payload.dto.QuestionOptionDto;
import org.springframework.stereotype.Component;

@Component
public class QuestionOptionMapper {

    public QuestionOptionDto toDto(QuestionOption questionOption, Boolean fromAdmin) {
        QuestionOptionDto optionDto = QuestionOptionDto.builder()
                .id(questionOption.getId())
                .optionText(questionOption.getOptionText())
                .build();
        if (fromAdmin) {
            optionDto.setIsCorrectAnswer(questionOption.getIsCorrectAnswer());
            optionDto.setSelectedTimes(questionOption.getSelectedTimes());
        }
        return optionDto;
    }

    public QuestionOption fromDto(QuestionOptionDto optionDto) {
        return QuestionOption.builder()
                .id(optionDto.getId())
                .optionText(optionDto.getOptionText())
                .build();
    }
}
