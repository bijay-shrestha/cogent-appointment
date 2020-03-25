package com.cogent.cogentappointment.client.repository.custom;

import com.cogent.cogentappointment.client.dto.response.dashboard.DashboardFeatureResponseDTO;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * @author Rupak
 */
@Repository
@Qualifier("adminDashboardRepositoryCustom")
public interface AdminDashboardRepositoryCustom {

    List<DashboardFeatureResponseDTO> fetchActiveDashboardFeatureByAdmin(Long adminId, Long hospitalId);
}
