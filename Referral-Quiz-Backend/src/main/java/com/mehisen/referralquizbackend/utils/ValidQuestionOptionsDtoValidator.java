package com.mehisen.referralquizbackend.utils;

import com.mehisen.referralquizbackend.exception.QuestionValidationException;
import com.mehisen.referralquizbackend.payload.dto.QuestionOptionDto;
import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;

import java.util.List;

public class ValidQuestionOptionsDtoValidator implements ConstraintValidator<ValidQuestionOptionsDto,List<QuestionOptionDto>> {

    @Override
    public boolean isValid(List<QuestionOptionDto> questionOptions, ConstraintValidatorContext context) {
        if (questionOptions == null || questionOptions.size() != 4) {
           throw new QuestionValidationException("Question Options Should have 4 options");
        }
        int numTrueValues = 0;
        for (QuestionOptionDto optionRequest : questionOptions) {
            if (optionRequest.getIsCorrectAnswer()) {
                numTrueValues++;
            }

            if(optionRequest.getOptionText().isEmpty()){
                throw new QuestionValidationException("Question Options Should have options with non empty text");
            }
        }
        if(numTrueValues != 1){
            throw new QuestionValidationException("Should have one answer");
        }
        return true;
    }
}