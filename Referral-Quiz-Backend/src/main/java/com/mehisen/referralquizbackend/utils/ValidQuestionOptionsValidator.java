package com.mehisen.referralquizbackend.utils;

import com.mehisen.referralquizbackend.exception.QuestionValidationException;
import com.mehisen.referralquizbackend.payload.request.AddQuestionRequest;
import com.mehisen.referralquizbackend.payload.request.OptionRequest;
import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;
import lombok.extern.slf4j.Slf4j;

import java.util.*;

public class ValidQuestionOptionsValidator implements ConstraintValidator<ValidQuestionOptions,List<OptionRequest>> {

    @Override
    public boolean isValid(List<OptionRequest> questionOptions, ConstraintValidatorContext context) {
        if (questionOptions == null || questionOptions.size() != 4) {
           throw new QuestionValidationException("Question Options Should have 4 options");
        }
        int numTrueValues = 0;
        for (OptionRequest optionRequest : questionOptions) {
            if (optionRequest.isCorrectAnswer()) {
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