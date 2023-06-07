package com.mehisen.referralquizbackend.models;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "redeems")
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Builder
public class Redeem {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    private Integer pointsToRedeem;

    private String redeemName;

    private boolean isAvailable;
}
