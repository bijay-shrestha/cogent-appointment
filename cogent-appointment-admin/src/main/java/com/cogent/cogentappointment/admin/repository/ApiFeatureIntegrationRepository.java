package com.cogent.cogentappointment.admin.repository;

import com.cogent.cogentappointment.persistence.model.ApiFeatureIntegration;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

/**
 * @author rupak on 2020-05-20
 */
@Repository
public interface ApiFeatureIntegrationRepository extends JpaRepository<ApiFeatureIntegration, Long> {
}
