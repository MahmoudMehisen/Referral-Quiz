package com.mehisen.referralquizbackend.payload.mapper;

import com.mehisen.referralquizbackend.models.Question;
import com.mehisen.referralquizbackend.models.QuestionGroup;
import com.mehisen.referralquizbackend.payload.dto.QuestionDto;
import com.mehisen.referralquizbackend.payload.dto.QuestionGroupDto;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Arrays;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyBoolean;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class QuestionGroupMapperTest {

    @Mock
    private QuestionMapper questionMapper;
    @InjectMocks
    private QuestionGroupMapper questionGroupMapper;

    @Test
    void toDtoWithoutQuestions() {
        QuestionGroup questionGroup = QuestionGroup.builder()
                .id(1L)
                .name("Test Question Group")
                .numberOfPlayed(5)
                .build();

        QuestionGroupDto questionGroupDto = questionGroupMapper.toDtoWithoutQuestions(questionGroup);

        assertThat(questionGroupDto.getId()).isEqualTo(questionGroup.getId());
        assertThat(questionGroupDto.getName()).isEqualTo(questionGroup.getName());
        assertThat(questionGroupDto.getNumberOfPlayed()).isEqualTo(questionGroup.getNumberOfPlayed());
        assertThat(questionGroupDto.getQuestions()).isNull();
    }

    @Test
    void toDto() {
        QuestionGroup questionGroup = QuestionGroup.builder()
                .id(1L)
                .name("Test Question Group")
                .numberOfPlayed(5)
                .questions(Arrays.asList(new Question(), new Question()))
                .build();

        QuestionDto questionDto = QuestionDto.builder()
                .id(1L)
                .questionText("Test Question")
                .build();

        when(questionMapper.toDto(any(Question.class), anyBoolean())).thenReturn(questionDto);

        QuestionGroupDto questionGroupDto = questionGroupMapper.toDto(questionGroup, true);

        assertThat(questionGroupDto.getId()).isEqualTo(questionGroup.getId());
        assertThat(questionGroupDto.getName()).isEqualTo(questionGroup.getName());
        assertThat(questionGroupDto.getNumberOfPlayed()).isEqualTo(questionGroup.getNumberOfPlayed());
        assertThat(questionGroupDto.getQuestions()).hasSize(2);
        assertThat(questionGroupDto.getQuestions().get(0)).isEqualTo(questionDto);
        assertThat(questionGroupDto.getQuestions().get(1)).isEqualTo(questionDto);

        verify(questionMapper, times(2)).toDto(any(Question.class), anyBoolean());
    }


    @Test
    void fromDto() {

        Question question = Question.builder()
                .id(1L)
                .questionText("Test Question")
                .build();

        QuestionGroupDto questionGroupDto = QuestionGroupDto.builder()
                .id(1L)
                .name("Test Question Group")
                .questions(Arrays.asList(new QuestionDto(), new QuestionDto()))
                .build();

        when(questionMapper.fromDto(any(QuestionDto.class))).thenReturn(question);

        QuestionGroup questionGroup = questionGroupMapper.fromDto(questionGroupDto);

        assertThat(questionGroup.getId()).isEqualTo(questionGroupDto.getId());
        assertThat(questionGroup.getName()).isEqualTo(questionGroupDto.getName());
        assertThat(questionGroup.getNumberOfPlayed()).isEqualTo(questionGroupDto.getNumberOfPlayed());
        assertThat(questionGroup.getQuestions()).hasSize(2);
        assertThat(questionGroup.getQuestions().get(0)).isEqualTo(question);
        assertThat(questionGroup.getQuestions().get(1)).isEqualTo(question);

        verify(questionMapper, times(2)).fromDto(any(QuestionDto.class));

    }
}