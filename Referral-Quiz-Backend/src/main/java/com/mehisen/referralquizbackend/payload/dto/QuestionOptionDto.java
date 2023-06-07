package com.mehisen.referralquizbackend.payload.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.*;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Builder
public class QuestionOptionDto {
    @NotNull
    private Long id;
    @NotBlank
    private String optionText;

    private Boolean isCorrectAnswer;

    private Integer selectedTimes;
}
