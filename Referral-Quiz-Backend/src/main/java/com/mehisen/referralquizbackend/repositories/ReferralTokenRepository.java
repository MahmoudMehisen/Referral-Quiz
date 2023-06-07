package com.mehisen.referralquizbackend.repositories;

import com.mehisen.referralquizbackend.models.ReferralToken;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ReferralTokenRepository extends JpaRepository<ReferralToken,String> {
}
