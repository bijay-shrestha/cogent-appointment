package com.cogent.cogentappointment.admin.repository;

import com.cogent.cogentappointment.persistence.model.IntegrationChannel;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface IntegrationChannelRepository extends JpaRepository<IntegrationChannel,Long> {

    @Query("SELECT ic FROM IntegrationChannel ic WHERE ic.status='Y' AND ic.id=:id")
    Optional<IntegrationChannel> findActiveIntegrationChannel(@Param("id") Long integrationChannelId);
}
