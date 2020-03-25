package com.cogent.cogentappointment.client.utils;

import com.cogent.cogentappointment.persistence.model.Admin;
import com.cogent.cogentappointment.persistence.model.AdminDashboardFeature;
import com.cogent.cogentappointment.persistence.model.DashboardFeature;

import java.util.ArrayList;
import java.util.List;

/**
 * @author Rupak
 */
public class DashboardFeatureUtils {

    public static List<AdminDashboardFeature> parseToAdminDashboardFeature(List<DashboardFeature> dashboardFeatureList, Admin admin) {

        List<AdminDashboardFeature> adminDashboardFeatureList = new ArrayList<>();
        dashboardFeatureList.forEach(dashboardFeature -> {
            AdminDashboardFeature adminDashboardFeature = new AdminDashboardFeature();
            adminDashboardFeature.setAdminId(admin);
            adminDashboardFeature.setDashboardFeatureId(dashboardFeature);
            adminDashboardFeature.setStatus('Y');
            adminDashboardFeatureList.add(adminDashboardFeature);

        });

        return adminDashboardFeatureList;
    }

    public static List<AdminDashboardFeature> parseToUpdatedAdminDashboardFeature(List<DashboardFeature> dashboardFeatureList, Admin admin) {

        List<AdminDashboardFeature> adminDashboardFeatureList = new ArrayList<>();
        dashboardFeatureList.forEach(dashboardFeature -> {
            AdminDashboardFeature adminDashboardFeature = new AdminDashboardFeature();
            adminDashboardFeature.setAdminId(admin);
            adminDashboardFeature.setDashboardFeatureId(dashboardFeature);
            adminDashboardFeature.setStatus('Y');
            adminDashboardFeatureList.add(adminDashboardFeature);

        });

        return adminDashboardFeatureList;
    }

}
