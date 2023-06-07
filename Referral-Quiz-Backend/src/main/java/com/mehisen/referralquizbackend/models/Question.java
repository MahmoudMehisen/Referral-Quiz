package com.mehisen.referralquizbackend.models;

import jakarta.persistence.*;
import lombok.*;

import java.util.List;

@Entity
@Table(name = "questions")
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Builder
public class Question {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @Column(nullable = false)
    private String questionText;

    @OneToMany(mappedBy = "question", cascade = CascadeType.ALL)
    private List<QuestionOption> options;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "group_id")
    private QuestionGroup questionGroup;
}