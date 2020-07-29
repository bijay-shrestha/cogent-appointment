package com.cogent.cogentappointment.admin.repository;

import com.cogent.cogentappointment.admin.repository.custom.AdminModeRequestHeaderRepositoryCustom;
import com.cogent.cogentappointment.persistence.model.AdminModeRequestHeader;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface AdminModeRequestHeaderRepository extends JpaRepository<AdminModeRequestHeader, Long>,
        AdminModeRequestHeaderRepositoryCustom {

    @Query("SELECT amrf FROM AdminModeRequestHeader amrf WHERE amrf.id=:id AND amrf.status!='D'")
    Optional<AdminModeRequestHeader> findAdminModeRequestHeaderById(@Param("id") Long id);

    @Query("SELECT amrf FROM AdminModeRequestHeader amrf WHERE amrf.apiIntegrationFormatId=:id AND amrf.status!='D'")
    Optional<List<AdminModeRequestHeader>> findAdminModeApiRequestHeaderByApiFeatureIntegrationId(@Param("id") Long id);
}
