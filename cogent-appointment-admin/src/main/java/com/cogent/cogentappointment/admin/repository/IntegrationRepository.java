package com.cogent.cogentappointment.admin.repository;

import com.cogent.cogentappointment.admin.constants.SwaggerConstants;
import com.cogent.cogentappointment.admin.repository.custom.IntegrationRepositoryCustom;
import com.cogent.cogentappointment.persistence.model.AdminModeFeatureIntegration;
import com.cogent.cogentappointment.persistence.model.ClientFeatureIntegration;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

/**
 * @author rupak on 2020-05-20
 */
@Repository
public interface IntegrationRepository extends JpaRepository<AdminModeFeatureIntegration,Long>, IntegrationRepositoryCustom {
}
