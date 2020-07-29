package com.cogent.cogentappointment.admin.repository.custom;

import com.cogent.cogentappointment.admin.dto.response.admin.AdminDropdownDTO;
import com.cogent.cogentappointment.admin.dto.response.dashboard.DashboardFeatureResponseDTO;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * @author Rupak
 */
@Repository
@Qualifier("adminDashboardRepositoryCustom")
public interface AdminDashboardRepositoryCustom {

    List<DashboardFeatureResponseDTO> fetchActiveDashboardFeatureByAdmin(Long adminId,Long hospitalId);
}
