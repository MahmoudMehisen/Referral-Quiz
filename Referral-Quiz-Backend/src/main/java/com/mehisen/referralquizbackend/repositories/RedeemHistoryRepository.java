package com.mehisen.referralquizbackend.repositories;

import com.mehisen.referralquizbackend.models.RedeemHistory;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface RedeemHistoryRepository extends JpaRepository<RedeemHistory, Long> {
}
