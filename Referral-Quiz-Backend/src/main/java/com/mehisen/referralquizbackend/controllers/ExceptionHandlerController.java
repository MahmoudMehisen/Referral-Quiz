package com.mehisen.referralquizbackend.controllers;

import com.infobip.ApiException;
import com.mehisen.referralquizbackend.exception.*;
import com.mehisen.referralquizbackend.payload.resonse.ErrorDetails;
import lombok.extern.slf4j.Slf4j;
import org.springframework.dao.DataAccessException;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.util.Date;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@RestControllerAdvice
@Slf4j
public class ExceptionHandlerController {

    @ExceptionHandler(MethodArgumentNotValidException.class)
    protected ResponseEntity<Object> handleMethodArgumentNotValid(MethodArgumentNotValidException ex) {
        Map<String, Object> responseBody = new LinkedHashMap<>();
        responseBody.put("timestamp", new Date());
        responseBody.put("status", ex.getStatusCode().value());

        List<String> errors = ex.getBindingResult().getFieldErrors()
                .stream()
                .map(error -> ((FieldError) error).getField() + ": " + error.getDefaultMessage())
                .collect(Collectors.toList());

        responseBody.put("errors", errors);

        return new ResponseEntity<>(responseBody, ex.getHeaders(), ex.getStatusCode());
    }

    @ExceptionHandler(QuestionValidationException.class)
    public ResponseEntity<ErrorDetails> questionValidationException(QuestionValidationException ex) {
        return new ResponseEntity<ErrorDetails>(new ErrorDetails(HttpStatus.BAD_REQUEST.value(), ex.getMessage()), HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(QuestionNotFoundException.class)
    public ResponseEntity<ErrorDetails> questionNotFoundException(QuestionNotFoundException ex) {
        return new ResponseEntity<ErrorDetails>(new ErrorDetails(HttpStatus.NOT_FOUND.value(), ex.getMessage()), HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler(MetadataNotFoundException.class)
    public ResponseEntity<ErrorDetails> questionNotFoundException(MetadataNotFoundException ex) {
        return new ResponseEntity<ErrorDetails>(new ErrorDetails(HttpStatus.NOT_FOUND.value(), ex.getMessage()), HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler(ReferralTokenExpiredException.class)
    public ResponseEntity<ErrorDetails> referralTokenExpiredException(ReferralTokenExpiredException ex) {
        return new ResponseEntity<ErrorDetails>(new ErrorDetails(HttpStatus.NOT_FOUND.value(), ex.getMessage()), HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler(ReferralTokenNotFoundException.class)
    public ResponseEntity<ErrorDetails> referralTokenNotFoundException(ReferralTokenNotFoundException ex) {
        return new ResponseEntity<ErrorDetails>(new ErrorDetails(HttpStatus.NOT_FOUND.value(), ex.getMessage()), HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler(DuplicateGuestEmailException.class)
    public ResponseEntity<ErrorDetails> duplicateGuestEmailException(DuplicateGuestEmailException ex) {
        return new ResponseEntity<ErrorDetails>(new ErrorDetails(HttpStatus.BAD_REQUEST.value(), ex.getMessage()), HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(GuestCanNotDoReferralException.class)
    public ResponseEntity<ErrorDetails> guestCanNotDoReferralException(GuestCanNotDoReferralException ex) {
        return new ResponseEntity<ErrorDetails>(new ErrorDetails(HttpStatus.BAD_REQUEST.value(), ex.getMessage()), HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(GuestNotFoundException.class)
    public ResponseEntity<ErrorDetails> guestNotFoundException(GuestNotFoundException ex) {
        return new ResponseEntity<ErrorDetails>(new ErrorDetails(HttpStatus.NOT_FOUND.value(), ex.getMessage()), HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler(GroupNotFoundException.class)
    public ResponseEntity<ErrorDetails> groupNotFoundException(GroupNotFoundException ex) {
        return new ResponseEntity<ErrorDetails>(new ErrorDetails(HttpStatus.NOT_FOUND.value(), ex.getMessage()), HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler(NoActiveGroupException.class)
    public ResponseEntity<ErrorDetails> noActiveGroupException(NoActiveGroupException ex) {
        return new ResponseEntity<ErrorDetails>(new ErrorDetails(HttpStatus.NOT_FOUND.value(), ex.getMessage()), HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler(InvalidRedeemRequestException.class)
    public ResponseEntity<ErrorDetails> invalidRedeemRequestException(InvalidRedeemRequestException ex) {
        return new ResponseEntity<ErrorDetails>(new ErrorDetails(HttpStatus.BAD_REQUEST.value(), ex.getMessage()), HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(RedeemNotFoundException.class)
    public ResponseEntity<ErrorDetails> redeemNotFoundException(RedeemNotFoundException ex) {
        return new ResponseEntity<ErrorDetails>(new ErrorDetails(HttpStatus.NOT_FOUND.value(), ex.getMessage()), HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler(ApiException.class)
    public ResponseEntity<ErrorDetails> sendSmsException(ApiException ex) {
        return new ResponseEntity<ErrorDetails>(new ErrorDetails(HttpStatus.INTERNAL_SERVER_ERROR.value(), ex.getMessage()), HttpStatus.INTERNAL_SERVER_ERROR);
    }


    @ExceptionHandler(InvalidOTPRedeemTokenException.class)
    public ResponseEntity<ErrorDetails> sendSmsException(InvalidOTPRedeemTokenException ex) {
        return new ResponseEntity<ErrorDetails>(new ErrorDetails(HttpStatus.BAD_REQUEST.value(), ex.getMessage()), HttpStatus.BAD_REQUEST);
    }


    @ExceptionHandler(Exception.class)
    public ResponseEntity<ErrorDetails> generalException(Exception ex) {
        return new ResponseEntity<ErrorDetails>(new ErrorDetails(HttpStatus.BAD_REQUEST.value(), ex.getMessage()), HttpStatus.BAD_REQUEST);
    }
}