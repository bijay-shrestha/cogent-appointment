package com.cogent.cogentappointment.admin.repository;

import com.cogent.cogentappointment.persistence.model.ApiFeatureIntegrationRequestBodyParameters;
import com.cogent.cogentappointment.persistence.model.ApiIntegrationRequestBodyParameters;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface ApiFeatureIntegrationRequestBodyParametersRepository extends
        JpaRepository<ApiFeatureIntegrationRequestBodyParameters, Long> {

    @Query("SELECT afirbp FROM ApiFeatureIntegrationRequestBodyParameters afirbp WHERE afirbp.featureId=:id AND afirbp.status!='D'")
    Optional<List<ApiFeatureIntegrationRequestBodyParameters>> findApiFeatureRequestBodyParameterByFeatureId(@Param("id") Long id);

    @Query("SELECT afirbp FROM ApiFeatureIntegrationRequestBodyParameters afirbp WHERE afirbp.featureId=:featureid AND afirbp.requestBodyParametersId=:requestbodyid AND afirbp.status!='D'")
    Optional<List<ApiFeatureIntegrationRequestBodyParameters>>  findByFeatureIdAndRequestBodyId(@Param("featureid") Long featureid,@Param("requestbodyid")  Long requestbodyid);
}
