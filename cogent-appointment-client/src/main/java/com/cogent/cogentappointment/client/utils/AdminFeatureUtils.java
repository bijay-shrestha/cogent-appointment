package com.cogent.cogentappointment.client.utils;

import com.cogent.cogentappointment.persistence.model.Admin;
import com.cogent.cogentappointment.persistence.model.AdminFeature;

/**
 * @author smriti on 18/04/20
 */
public class AdminFeatureUtils {

    public static AdminFeature parseToAdminFeature(Admin admin,
                                                   Character isSideBarCollapse) {
        AdminFeature adminFeature = new AdminFeature();
        adminFeature.setAdmin(admin);
        adminFeature.setIsSideBarCollapse(isSideBarCollapse);
        return adminFeature;
    }

    public static void updateAdminFeatureFlag(AdminFeature adminFeature, Character isSideBarCollapse) {
        adminFeature.setIsSideBarCollapse(isSideBarCollapse);
    }
}
