package com.cogent.cogentappointment.admin.utils;

import com.cogent.cogentappointment.persistence.model.Admin;
import com.cogent.cogentappointment.persistence.model.AdminDashboardFeature;
import com.cogent.cogentappointment.persistence.model.DashboardFeature;

import java.util.ArrayList;
import java.util.List;

import static com.cogent.cogentappointment.admin.constants.StatusConstants.ACTIVE;

/**
 * @author Rupak
 */
public class DashboardFeatureUtils {

    public static List<AdminDashboardFeature> parseToAdminDashboardFeature(List<DashboardFeature> dashboardFeatureList,
                                                                           Admin admin) {

        List<AdminDashboardFeature> adminDashboardFeatureList = new ArrayList<>();
        dashboardFeatureList.forEach(dashboardFeature -> {
            AdminDashboardFeature adminDashboardFeature = new AdminDashboardFeature();
            adminDashboardFeature.setAdminId(admin);
            adminDashboardFeature.setDashboardFeatureId(dashboardFeature);
            adminDashboardFeature.setStatus(ACTIVE);
            adminDashboardFeatureList.add(adminDashboardFeature);
        });

        return adminDashboardFeatureList;
    }

    public static List<AdminDashboardFeature> parseToUpdateAdminDashboardFeature(List<DashboardFeature> dashboardFeatureList,Character status,
                                                                           Admin admin) {

        List<AdminDashboardFeature> adminDashboardFeatureList = new ArrayList<>();
        dashboardFeatureList.forEach(dashboardFeature -> {
            AdminDashboardFeature adminDashboardFeature = new AdminDashboardFeature();
            adminDashboardFeature.setAdminId(admin);
            adminDashboardFeature.setDashboardFeatureId(dashboardFeature);
            adminDashboardFeature.setStatus(status);
            adminDashboardFeatureList.add(adminDashboardFeature);
        });

        return adminDashboardFeatureList;
    }

}
