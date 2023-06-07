package com.mehisen.referralquizbackend.payload.request;


import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class SubmitRedeemRequest {
    private String guestPhone;
    private Long redeemId;
}
