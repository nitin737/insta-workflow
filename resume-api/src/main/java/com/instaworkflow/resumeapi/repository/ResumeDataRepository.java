package com.instaworkflow.resumeapi.repository;

import com.instaworkflow.resumeapi.model.ResumeData;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;
import java.util.UUID;

@Repository
public interface ResumeDataRepository extends JpaRepository<ResumeData, UUID> {
    Optional<ResumeData> findByIdAndTenantId(UUID id, Long tenantId);
}
