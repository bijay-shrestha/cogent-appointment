package com.cogent.cogentappointment.admin.repository;

import com.cogent.cogentappointment.persistence.model.ApiQueryParameters;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

/**
 * @author rupak on 2020-05-19
 */
@Repository
public interface ApiQueryParametersRepository extends JpaRepository<ApiQueryParameters,Long> {

    @Query("SELECT aqp FROM ApiQueryParameters aqp WHERE aqp.id=:id and aqp.status!='D'")
    Optional<ApiQueryParameters> findApiQueryParameterById(@Param("id") Long id);

    @Query("SELECT aqp FROM ApiQueryParameters aqp WHERE aqp.apiIntegrationFormatId=:id and aqp.status!='D'")
    Optional<List<ApiQueryParameters>> findApiRequestHeaderByApiFeatureIntegrationId(@Param("id") Long id);
}
