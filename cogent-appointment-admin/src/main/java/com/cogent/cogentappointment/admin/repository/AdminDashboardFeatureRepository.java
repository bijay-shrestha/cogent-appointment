package com.cogent.cogentappointment.admin.repository;

import com.cogent.cogentappointment.persistence.model.AdminDashboardFeature;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

/**
 * @author Rupak
 */
@Repository
public interface AdminDashboardFeatureRepository extends JpaRepository<AdminDashboardFeature,Long> {
}
