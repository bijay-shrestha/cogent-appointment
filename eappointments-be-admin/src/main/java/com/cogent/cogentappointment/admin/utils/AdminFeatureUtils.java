package com.cogent.cogentappointment.admin.utils;

import com.cogent.cogentappointment.persistence.model.Admin;
import com.cogent.cogentappointment.persistence.model.AdminFeature;

import static com.cogent.cogentappointment.admin.constants.StatusConstants.YES;

/**
 * @author smriti on 18/04/20
 */
public class AdminFeatureUtils {

    public static AdminFeature parseToAdminFeature(Admin admin) {
        AdminFeature adminFeature = new AdminFeature();
        adminFeature.setAdmin(admin);
        adminFeature.setIsSideBarCollapse(YES);
        return adminFeature;
    }

    public static void updateAdminFeatureFlag(AdminFeature adminFeature, Character isSideBarCollapse) {
        adminFeature.setIsSideBarCollapse(isSideBarCollapse);
    }
}
