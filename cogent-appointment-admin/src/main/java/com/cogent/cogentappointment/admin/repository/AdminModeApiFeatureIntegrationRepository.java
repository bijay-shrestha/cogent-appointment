package com.cogent.cogentappointment.admin.repository;

import com.cogent.cogentappointment.admin.repository.custom.AdminModeApiFeatureIntegrationRepositoryCustom;
import com.cogent.cogentappointment.persistence.model.AdminModeApiFeatureIntegration;
import com.cogent.cogentappointment.persistence.model.AdminModeFeatureIntegration;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

/**
 * @author rupak on 2020-05-21
 */
@Repository
public interface AdminModeApiFeatureIntegrationRepository extends JpaRepository<AdminModeApiFeatureIntegration, Long>,
        AdminModeApiFeatureIntegrationRepositoryCustom {


}
