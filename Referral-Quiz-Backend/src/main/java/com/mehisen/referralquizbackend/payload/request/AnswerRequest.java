package com.mehisen.referralquizbackend.payload.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class AnswerRequest {
    @NotBlank
    private String phoneNumber;
    private List<AnswerQuestionOption> answerQuestionOptionList;
}
