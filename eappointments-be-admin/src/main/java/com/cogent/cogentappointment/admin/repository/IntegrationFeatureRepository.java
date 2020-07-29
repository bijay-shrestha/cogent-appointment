package com.cogent.cogentappointment.admin.repository;

import com.cogent.cogentappointment.admin.repository.custom.IntegrationFeatureRepositoryCustom;
import com.cogent.cogentappointment.persistence.model.Feature;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

/**
 * @author rupak on 2020-05-19
 */
@Repository
public interface IntegrationFeatureRepository extends JpaRepository<Feature,Long>, IntegrationFeatureRepositoryCustom {
}
