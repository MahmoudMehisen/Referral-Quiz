package com.mehisen.referralquizbackend.models;

import jakarta.persistence.*;
import lombok.*;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Entity
@Table(name = "metadata")
@Builder
public class Metadata {
    @Id
    private Long id;

    private Integer pointsPerQuestion;

    private Integer pointsPerReferral;

    private Integer referralExpirationTime;

    private boolean canUserDoReferral;

    private Long activeGroupId;
}
