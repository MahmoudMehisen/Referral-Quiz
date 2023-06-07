package com.mehisen.referralquizbackend.payload.request;

import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class AnswerQuestionOption {
    @NotNull
    private Long questionId;
    @NotNull
    private Long optionId;
}
