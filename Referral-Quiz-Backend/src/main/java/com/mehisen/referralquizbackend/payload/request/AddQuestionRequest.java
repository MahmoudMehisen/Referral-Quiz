package com.mehisen.referralquizbackend.payload.request;

import com.mehisen.referralquizbackend.utils.ValidQuestionOptions;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.*;

import java.util.List;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class AddQuestionRequest {

    @NotNull
    private Long groupId;

    @NotBlank(message = "Please Enter Question Text")
    private String questionText;

    @ValidQuestionOptions(message = "All elements in questionOptions list should have 4 options and only one of this has isTheAnswer true")
    private List<OptionRequest> questionOptions;
}
