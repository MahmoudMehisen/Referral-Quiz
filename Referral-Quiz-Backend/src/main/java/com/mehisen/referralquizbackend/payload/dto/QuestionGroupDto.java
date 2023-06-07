package com.mehisen.referralquizbackend.payload.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.*;

import java.util.List;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Builder
public class QuestionGroupDto {
    @NotNull
    private Long id;
    @NotBlank
    private String name;
    private Integer numberOfPlayed;
    private List<QuestionDto> questions;
}
