package com.mehisen.referralquizbackend.models;

import jakarta.persistence.*;
import lombok.*;

import java.util.List;

@Entity
@Table(name = "question_groups")
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Builder
public class QuestionGroup {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    private String name;

    private Integer numberOfPlayed;

    @OneToMany(mappedBy = "questionGroup",fetch = FetchType.LAZY ,cascade = CascadeType.ALL)
    private List<Question> questions;
}
