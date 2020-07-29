package com.cogent.cogentappointment.admin.repository;

import com.cogent.cogentappointment.admin.repository.custom.AdminModeQueryParametersRepositoryCustom;
import com.cogent.cogentappointment.persistence.model.AdminModeQueryParameters;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface AdminModeQueryParametersRepository extends JpaRepository<AdminModeQueryParameters,Long>
,AdminModeQueryParametersRepositoryCustom {

    @Query("SELECT amqr FROM AdminModeQueryParameters amqr WHERE amqr.id=:id AND amqr.status!='D'")
    Optional<AdminModeQueryParameters> findAdminModeQueryParametersById(@Param("id") Long id);

    @Query("SELECT amqr FROM AdminModeQueryParameters amqr WHERE amqr.apiIntegrationFormatId=:id AND amqr.status!='D'")
    Optional<List<AdminModeQueryParameters>> findApiRequestHeaderByApiIntegrationFormatId(@Param("id") Long id);
}
