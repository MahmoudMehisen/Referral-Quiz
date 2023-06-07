package com.mehisen.referralquizbackend.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(value = HttpStatus.NOT_FOUND)
public class ReferralTokenNotFoundException extends RuntimeException {
    public ReferralTokenNotFoundException(String message){
        super(message);
    }
}
