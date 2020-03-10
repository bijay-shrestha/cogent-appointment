package com.cogent.cogentappointment.admin.utils.commons;

import com.cogent.cogentappointment.admin.dto.request.admin.AdminDashboardRequestDTO;
import com.cogent.cogentappointment.persistence.model.Admin;
import com.cogent.cogentappointment.persistence.model.AdminDashboardFeature;
import com.cogent.cogentappointment.persistence.model.DashboardFeature;

import java.util.ArrayList;
import java.util.List;

/**
 * @author Rupak
 */
public class DashboardFeatureUtils {

    public static List<AdminDashboardFeature> parseToAdminDashboardFeature(List<AdminDashboardRequestDTO> requestDTOList, Admin admin) {

        List<AdminDashboardFeature> adminDashboardFeatureList = new ArrayList<>();
        requestDTOList.forEach(dashboardFeature -> {

            AdminDashboardFeature adminDashboardFeature = new AdminDashboardFeature();
            adminDashboardFeature.setAdminId(admin);
            adminDashboardFeature.setDashboardFeatureId(dashboardFeature.getId());
            adminDashboardFeature.setStatus('Y');

            adminDashboardFeatureList.add(adminDashboardFeature);

        });

        return adminDashboardFeatureList;
    }
}
