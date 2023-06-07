package com.mehisen.referralquizbackend.models;


import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;

@Entity
@Table(name = "otp_redeems")
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Builder
public class OTPRedeem {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    private String otpToken;
    @ManyToOne
    @JoinColumn(name = "guest_phone_number")
    private Guest guest;
    @ManyToOne
    @JoinColumn(name = "redeem_id")
    private Redeem redeem;
    @Column(name = "expiry_date")
    private LocalDateTime expiryDate;
}
