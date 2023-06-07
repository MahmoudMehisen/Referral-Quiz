package com.mehisen.referralquizbackend.payload.request;

import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class OptionRequest{
    @NotBlank
    private String optionText;

    @NotNull
    @JsonProperty("isCorrectAnswer")
    private boolean isCorrectAnswer;
}