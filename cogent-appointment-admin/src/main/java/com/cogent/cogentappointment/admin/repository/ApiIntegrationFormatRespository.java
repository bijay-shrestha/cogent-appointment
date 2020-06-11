package com.cogent.cogentappointment.admin.repository;

import com.cogent.cogentappointment.persistence.model.ApiIntegrationFormat;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Optional;

/**
 * @author rupak on 2020-05-19
 */
@Repository
public interface ApiIntegrationFormatRespository extends JpaRepository<ApiIntegrationFormat,Long> {

    @Query("SELECT aif FROM ApiIntegrationFormat aif WHERE aif.id=:id and aif.status!='D'")
    Optional<ApiIntegrationFormat >findByIntegrationFormatId(@Param("id") Long apiIntegrationFormatId);
}
