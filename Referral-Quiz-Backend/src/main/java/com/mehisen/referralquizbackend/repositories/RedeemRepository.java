package com.mehisen.referralquizbackend.repositories;

import com.mehisen.referralquizbackend.models.Redeem;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface RedeemRepository extends JpaRepository<Redeem, Long> {
    List<Redeem> findAllByIsAvailable(boolean isAvailable);
}
