package com.cogent.cogentappointment.admin.repository;

import com.cogent.cogentappointment.persistence.model.AdminFeature;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Optional;

/**
 * @author smriti on 18/04/20
 */
@Repository
public interface AdminFeatureRepository extends JpaRepository<AdminFeature, Long> {

    @Query("SELECT a FROM AdminFeature a WHERE a.admin.id = :adminId AND a.admin.status!='D'")
    Optional<AdminFeature> findAdminFeatureByAdminId(@Param("adminId") Long adminId);
}
