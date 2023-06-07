package com.mehisen.referralquizbackend.repositories;

import com.mehisen.referralquizbackend.models.Metadata;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;


@Repository
public interface MetadataRepository extends JpaRepository<Metadata, Long> {

}
