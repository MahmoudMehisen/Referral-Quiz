package com.mehisen.referralquizbackend.models;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "redeem_histories")
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Builder
public class RedeemHistory {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "guest_phone_number")
    private Guest guest;

    @ManyToOne
    @JoinColumn(name = "redeem_id")
    private Redeem redeem;

    private Integer pointsForRedeem;
}
