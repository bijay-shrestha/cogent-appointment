package com.cogent.cogentappointment.admin.repository;

import com.cogent.cogentappointment.persistence.model.AdminDashboardFeature;
import com.cogent.cogentappointment.persistence.model.AppointmentRefundDetail;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Optional;

/**
 * @author Rupak
 */
@Repository
public interface AdminDashboardFeatureRepository extends JpaRepository<AdminDashboardFeature,Long> {

    @Query("SELECT adf FROM AdminDashboardFeature adf WHERE adf.dashboardFeatureId.id =:id AND adf.adminId.id=:adminid AND adf.status!='D'")
    Optional<AdminDashboardFeature> findAdminDashboardFeatureBydashboardFeatureId(@Param("id") Long dashboardFeatureId,@Param("adminid")Long adminId);
}
