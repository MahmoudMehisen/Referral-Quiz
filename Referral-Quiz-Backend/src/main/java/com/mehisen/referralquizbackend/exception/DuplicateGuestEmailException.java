package com.mehisen.referralquizbackend.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(value = HttpStatus.BAD_REQUEST)
public class DuplicateGuestEmailException extends RuntimeException {
    public DuplicateGuestEmailException(String message){
        super(message);
    }
}
