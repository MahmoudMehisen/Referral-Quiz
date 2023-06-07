package com.mehisen.referralquizbackend.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(value = HttpStatus.BAD_REQUEST)
public class QuestionValidationException extends RuntimeException {
    public QuestionValidationException(String message){
        super(message);
    }
}
