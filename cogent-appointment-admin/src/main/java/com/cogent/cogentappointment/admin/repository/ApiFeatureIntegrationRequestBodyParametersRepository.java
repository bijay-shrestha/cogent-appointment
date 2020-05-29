package com.cogent.cogentappointment.admin.repository;

import com.cogent.cogentappointment.persistence.model.ApiFeatureIntegrationRequestBodyParameters;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ApiFeatureIntegrationRequestBodyParametersRepository extends
        JpaRepository<ApiFeatureIntegrationRequestBodyParameters,Long>{

}
