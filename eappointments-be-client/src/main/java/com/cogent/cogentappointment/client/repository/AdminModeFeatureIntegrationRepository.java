package com.cogent.cogentappointment.client.repository;

import com.cogent.cogentappointment.client.repository.custom.AdminModeFeatureIntegrationRepositoryCustom;
import com.cogent.cogentappointment.persistence.model.AdminModeFeatureIntegration;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Optional;

/**
 * @author rupak ON 2020/06/03-2:03 PM
 */
@Repository
public interface AdminModeFeatureIntegrationRepository extends JpaRepository<AdminModeFeatureIntegration
        ,Long>,AdminModeFeatureIntegrationRepositoryCustom {

    @Query("SELECT amfi FROM AdminModeFeatureIntegration amfi WHERE amfi.status!='D' AND amfi.id=:id")
    Optional<AdminModeFeatureIntegration> findAdminModeFeatureIntegrationById(@Param("id") Long id);

}
