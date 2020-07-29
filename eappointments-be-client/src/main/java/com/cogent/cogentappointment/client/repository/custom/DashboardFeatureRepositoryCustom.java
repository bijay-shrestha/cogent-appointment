package com.cogent.cogentappointment.client.repository.custom;

import com.cogent.cogentappointment.persistence.model.DashboardFeature;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * @author Rupak
 */
@Repository
@Qualifier("dashboardFeatureRepositoryCustom")
public interface DashboardFeatureRepositoryCustom {

    List<DashboardFeature> validateDashboardFeatureCount(String ids);
}
