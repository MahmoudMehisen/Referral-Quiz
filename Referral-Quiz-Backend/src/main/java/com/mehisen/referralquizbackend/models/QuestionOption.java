package com.mehisen.referralquizbackend.models;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "question_options")
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Builder
public class QuestionOption {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @Column(nullable = false)
    private String optionText;

    @Column(nullable = false)
    private Boolean isCorrectAnswer;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "question_id")
    private Question question;

    private Integer selectedTimes;
}

