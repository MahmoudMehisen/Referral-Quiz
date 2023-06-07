package com.mehisen.referralquizbackend.utils;

import jakarta.validation.Constraint;
import jakarta.validation.Payload;

import java.lang.annotation.*;


@Target({ElementType.FIELD, ElementType.PARAMETER})
@Retention(RetentionPolicy.RUNTIME)
@Constraint(validatedBy = ValidQuestionOptionsValidator.class)
@Documented
public @interface ValidQuestionOptions {

    String message() default "Invalid question options";

    Class<?>[] groups() default {};

    Class<? extends Payload>[] payload() default {};
}
