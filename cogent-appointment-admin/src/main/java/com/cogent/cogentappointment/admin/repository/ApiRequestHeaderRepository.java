package com.cogent.cogentappointment.admin.repository;

import com.cogent.cogentappointment.persistence.model.ApiRequestHeader;
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
public interface ApiRequestHeaderRepository extends JpaRepository<ApiRequestHeader,Long> {

    @Query("SELECT arf FROM ApiRequestHeader arf WHERE arf.id=:id AND arf.status!='D'")
    Optional<ApiRequestHeader> findApiRequestHeaderById(@Param("id") Long id);

    @Query("SELECT arf FROM ApiRequestHeader arf WHERE arf.apiIntegrationFormatId=:id AND arf.status!='D'")
    Optional<List<ApiRequestHeader>> findApiRequestHeaderByApiFeatureIntegrationId(@Param("id") Long id);
}
