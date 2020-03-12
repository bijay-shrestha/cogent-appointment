package com.cogent.cogentappointment.admin.repository;

import com.cogent.cogentappointment.persistence.model.AdminMetaInfo;
import com.cogent.cogentappointment.persistence.model.DashboardFeature;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Optional;

/**
 * @author Rupak
 */
@Repository
public interface DashboardFeatureRepository extends JpaRepository<DashboardFeature,Long> {

    @Query("SELECT df FROM DashboardFeature df WHERE df.id = :id AND df.status!='D'")
    Optional<DashboardFeature> findActiveDashboardFeatureById(@Param("id") Long id);
}
