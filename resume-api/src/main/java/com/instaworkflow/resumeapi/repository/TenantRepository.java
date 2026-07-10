package com.instaworkflow.resumeapi.repository;

import com.instaworkflow.resumeapi.model.Tenant;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface TenantRepository extends JpaRepository<Tenant, Long> {
    Optional<Tenant> findByApiKeyHash(String apiKeyHash);
}
