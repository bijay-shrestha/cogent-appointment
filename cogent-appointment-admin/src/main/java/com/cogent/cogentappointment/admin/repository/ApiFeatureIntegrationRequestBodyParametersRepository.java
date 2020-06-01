package com.cogent.cogentappointment.admin.repository;

import com.cogent.cogentappointment.persistence.model.ApiFeatureIntegrationRequestBodyParameters;
import com.cogent.cogentappointment.persistence.model.ApiIntegrationRequestBodyParameters;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface ApiFeatureIntegrationRequestBodyParametersRepository extends
        JpaRepository<ApiFeatureIntegrationRequestBodyParameters, Long> {

    @Query("SELECT afirbp FROM ApiFeatureIntegrationRequestBodyParameters afirbp WHERE afirbp.id=:id AND afirbp.status!='D'")
    Optional<ApiFeatureIntegrationRequestBodyParameters> findApiFeatureRequestBodyParameterById(@Param("id") Long id);

}
