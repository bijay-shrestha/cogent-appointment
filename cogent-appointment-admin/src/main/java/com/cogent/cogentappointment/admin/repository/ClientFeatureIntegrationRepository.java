package com.cogent.cogentappointment.admin.repository;

import com.cogent.cogentappointment.persistence.model.ClientFeatureIntegration;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

/**
 * @author rupak on 2020-05-19
 */
@Repository
public interface ClientFeatureIntegrationRepository extends JpaRepository<ClientFeatureIntegration,Long> {
}
