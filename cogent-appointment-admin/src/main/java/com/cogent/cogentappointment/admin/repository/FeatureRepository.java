package com.cogent.cogentappointment.admin.repository;

import com.cogent.cogentappointment.persistence.model.Feature;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Optional;

/**
 * @author rupak on 2020-05-20
 */
@Repository
public interface FeatureRepository extends JpaRepository<Feature,Long> {

    @Query(" SELECT f FROM Feature f WHERE f.id=:id AND f.status = 'Y'")
    Optional<Feature> findFeatureById(@Param("id") Long featureTypeId);
}
