package com.mehisen.referralquizbackend.payload.mapper;

import com.mehisen.referralquizbackend.models.QuestionOption;
import com.mehisen.referralquizbackend.payload.dto.QuestionOptionDto;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(MockitoExtension.class)
class QuestionOptionMapperTest {

    @InjectMocks
    QuestionOptionMapper questionOptionMapper;

    @Test
    void toDto() {
        QuestionOption questionOption = QuestionOption.builder()
                .id(1L)
                .optionText("Option A")
                .isCorrectAnswer(true)
                .selectedTimes(10)
                .build();
        Boolean fromAdmin = true;

        QuestionOptionDto optionDto = questionOptionMapper.toDto(questionOption, fromAdmin);

        assertEquals(optionDto.getId(), 1l);
        assertEquals(optionDto.getOptionText(), "Option A");
        assertEquals(optionDto.getIsCorrectAnswer(), true);
        assertEquals(optionDto.getSelectedTimes(), 10);
    }

    @Test
    void fromDto() {
        QuestionOptionDto optionDto = QuestionOptionDto.builder()
                .id(1L)
                .optionText("Option A")
                .build();


        QuestionOption questionOption = questionOptionMapper.fromDto(optionDto);

        assertEquals(questionOption.getId(), 1L);
        assertEquals(questionOption.getOptionText(), "Option A");
    }
}