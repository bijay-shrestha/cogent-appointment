package com.cogent.cogentappointment.admin.repository;

import com.cogent.cogentappointment.admin.repository.custom.AdminModeApiFeatureIntegrationRepositoryCustom;
import com.cogent.cogentappointment.persistence.model.AdminModeApiFeatureIntegration;
import com.cogent.cogentappointment.persistence.model.AdminModeFeatureIntegration;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

/**
 * @author rupak on 2020-05-21
 */
@Repository
public interface AdminModeApiFeatureIntegrationRepository extends JpaRepository<AdminModeApiFeatureIntegration, Long>,
        AdminModeApiFeatureIntegrationRepositoryCustom {

    @Query("SELECT amafi FROM AdminModeApiFeatureIntegration amafi WHERE amafi.status!='D' AND amafi.adminModeFeatureIntegrationId.id=:id")
    Optional<List<AdminModeApiFeatureIntegration>> findAdminModeApiFeatureIntegrationbyAdminModeFeatureId(@Param("id") Long adminModeFeatureId);

}
