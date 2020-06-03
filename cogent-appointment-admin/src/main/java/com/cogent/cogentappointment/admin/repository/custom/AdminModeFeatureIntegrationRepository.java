package com.cogent.cogentappointment.admin.repository.custom;

import com.cogent.cogentappointment.persistence.model.AdminModeFeatureIntegration;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

/**
 * @author rupak ON 2020/06/03-2:03 PM
 */
@Repository
public interface AdminModeFeatureIntegrationRepository extends JpaRepository<AdminModeFeatureIntegration
        ,Long>,AdminModeFeatureIntegrationRepositoryCustom{
}
