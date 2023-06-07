package com.mehisen.referralquizbackend.models;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.*;

@Entity
@Table(name = "tokens")
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class ReferralToken {
    @Id
    private String token;
    private boolean active;
}
