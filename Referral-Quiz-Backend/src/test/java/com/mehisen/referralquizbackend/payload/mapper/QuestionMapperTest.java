package com.mehisen.referralquizbackend.payload.mapper;

import com.mehisen.referralquizbackend.models.Question;
import com.mehisen.referralquizbackend.models.QuestionOption;
import com.mehisen.referralquizbackend.payload.dto.QuestionDto;
import com.mehisen.referralquizbackend.payload.dto.QuestionOptionDto;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
@ExtendWith(MockitoExtension.class)
class QuestionMapperTest {

    @Mock
    private QuestionOptionMapper questionOptionMapper;
    @InjectMocks
    private QuestionMapper questionMapper;
    @Test
    void toDto() {
        Question question = new Question();
        question.setId(1L);
        question.setQuestionText("What is the capital of France?");

        QuestionOption option1 = new QuestionOption();
        option1.setId(1L);
        option1.setOptionText("Paris");
        option1.setIsCorrectAnswer(true);

        QuestionOption option2 = new QuestionOption();
        option2.setId(2L);
        option2.setOptionText("Madrid");
        option2.setIsCorrectAnswer(false);

        List<QuestionOption> options = Arrays.asList(option1, option2);
        question.setOptions(options);

        QuestionDto questionDto = questionMapper.toDto(question, true);

        assertEquals(question.getId(), questionDto.getId());
        assertEquals(question.getQuestionText(), questionDto.getQuestionText());
        assertEquals(question.getOptions().size(), questionDto.getOptions().size());
    }

    @Test
    void fromDto() {
        QuestionDto questionDto = new QuestionDto();
        questionDto.setId(1L);
        questionDto.setQuestionText("What is the capital of France?");

        QuestionOptionMapper questionOptionMapper = new QuestionOptionMapper();
        QuestionOptionDto optionDto1 = new QuestionOptionDto();
        optionDto1.setId(1L);
        optionDto1.setOptionText("Paris");
        optionDto1.setIsCorrectAnswer(true);

        QuestionOptionDto optionDto2 = new QuestionOptionDto();
        optionDto2.setId(2L);
        optionDto2.setOptionText("Madrid");
        optionDto2.setIsCorrectAnswer(false);

        List<QuestionOptionDto> optionDtos = Arrays.asList(optionDto1, optionDto2);
        questionDto.setOptions(optionDtos);

        Question question = questionMapper.fromDto(questionDto);

        assertEquals(questionDto.getId(), question.getId());
        assertEquals(questionDto.getQuestionText(), question.getQuestionText());
        assertEquals(questionDto.getOptions().size(), question.getOptions().size());
    }
}