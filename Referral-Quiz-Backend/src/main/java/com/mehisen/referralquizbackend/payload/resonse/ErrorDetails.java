package com.mehisen.referralquizbackend.payload.resonse;

import lombok.AllArgsConstructor;
import lombok.Getter;


@AllArgsConstructor
@Getter
public class ErrorDetails {
    private Integer status;
    private String message;
}
