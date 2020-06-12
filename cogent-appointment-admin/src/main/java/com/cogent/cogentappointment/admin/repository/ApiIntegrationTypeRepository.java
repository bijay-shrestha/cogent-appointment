package com.cogent.cogentappointment.admin.repository;

import com.cogent.cogentappointment.admin.repository.custom.ApiIntegrationTypeRepositoryCustom;
import com.cogent.cogentappointment.persistence.model.ApiIntegrationType;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

/**
 * @author rupak on 2020-05-26
 */
@Repository
public interface ApiIntegrationTypeRepository extends JpaRepository<ApiIntegrationType,Long>, ApiIntegrationTypeRepositoryCustom {

}
