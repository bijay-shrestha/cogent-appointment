package com.cogent.cogentappointment.admin.repository;

import com.cogent.cogentappointment.persistence.model.DashboardFeature;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

/**
 * @author Rupak
 */
@Repository
public interface DashboardFeatureRepository extends JpaRepository<DashboardFeature,Long> {
}
