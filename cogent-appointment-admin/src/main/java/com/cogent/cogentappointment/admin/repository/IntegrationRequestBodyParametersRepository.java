package com.cogent.cogentappointment.admin.repository;

import com.cogent.cogentappointment.admin.repository.custom.IntegrationRequestBodyParametersRepositoryCustom;
import com.cogent.cogentappointment.persistence.model.ApiIntegrationRequestBodyParameters;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface IntegrationRequestBodyParametersRepository extends
        JpaRepository<ApiIntegrationRequestBodyParameters,Long>,IntegrationRequestBodyParametersRepositoryCustom {
}
