package com.cogent.cogentappointment.admin.repository;

import com.cogent.cogentappointment.admin.repository.custom.IntegrationRequestBodyParametersRepositoryCustom;
import com.cogent.cogentappointment.persistence.model.ApiIntegrationRequestBodyParameters;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import javax.validation.constraints.NotNull;
import java.util.List;
import java.util.Optional;

@Repository
public interface IntegrationRequestBodyParametersRepository extends
        JpaRepository<ApiIntegrationRequestBodyParameters, Long>, IntegrationRequestBodyParametersRepositoryCustom {

    @Query("SELECT airbp FROM ApiIntegrationRequestBodyParameters airbp WHERE airbp.id=:id AND airbp.status!='D'")
    Optional<ApiIntegrationRequestBodyParameters> findRequestBodyParamterById(@Param("id") Long id);

}