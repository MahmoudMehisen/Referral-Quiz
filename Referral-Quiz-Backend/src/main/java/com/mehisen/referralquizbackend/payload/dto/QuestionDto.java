package com.mehisen.referralquizbackend.payload.dto;

import com.mehisen.referralquizbackend.utils.ValidQuestionOptionsDto;
import lombok.*;

import java.util.List;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Builder
public class QuestionDto {
    private Long id;
    private String questionText;
    @ValidQuestionOptionsDto(message = "All elements in questionOptions list should have 4 options and only one of this has isTheAnswer true")
    private List<QuestionOptionDto> options;
}
