package com.cogent.cogentappointment.client.repository;

import com.cogent.cogentappointment.client.repository.custom.IntegrationRequestBodyParametersRepositoryCustom;
import com.cogent.cogentappointment.persistence.model.ApiIntegrationRequestBodyParameters;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface IntegrationRequestBodyParametersRepository extends
        JpaRepository<ApiIntegrationRequestBodyParameters, Long>,
        IntegrationRequestBodyParametersRepositoryCustom {

//    @Query("SELECT airbp FROM ApiIntegrationRequestBodyParameters airbp where airbp.status='Y' AND airbp.id IN (:id)")
//    Optional<List<ApiIntegrationRequestBodyParameters>>
//    findActiveRequestBodyParameterByIds(@Param("id") String ids);
}