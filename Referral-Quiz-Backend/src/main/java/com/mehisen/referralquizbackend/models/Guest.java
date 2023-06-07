package com.mehisen.referralquizbackend.models;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.*;

@Entity
@Table(name = "guests")
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Builder
public class Guest {
    @Id
    private String phoneNumber;

    private String email;

    private Integer totalPoints;

    private Boolean canPlay;
}
