package com.mehisen.referralquizbackend.repositories;

import com.mehisen.referralquizbackend.models.QuestionGroup;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface QuestionGroupsRepository extends JpaRepository<QuestionGroup, Long> {
}
