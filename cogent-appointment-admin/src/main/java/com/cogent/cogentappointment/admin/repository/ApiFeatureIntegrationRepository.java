package com.cogent.cogentappointment.admin.repository;

import com.cogent.cogentappointment.persistence.model.ApiFeatureIntegration;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

/**
 * @author rupak on 2020-05-20
 */
@Repository
public interface ApiFeatureIntegrationRepository extends JpaRepository<ApiFeatureIntegration, Long> {

    @Query("SELECT afi FROM ApiFeatureIntegration afi WHERE afi.clientFeatureIntegrationId=:id AND afi.status!='D'")
    Optional<List<ApiFeatureIntegration>> findApiFeatureIntegrationbyClientFeatureId(@Param("id") Long id);
}
