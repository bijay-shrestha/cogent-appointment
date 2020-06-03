package com.cogent.cogentappointment.admin.utils;

import com.cogent.cogentappointment.admin.dto.commons.DeleteRequestDTO;
import com.cogent.cogentappointment.persistence.model.AdminModeApiFeatureIntegration;
import com.cogent.cogentappointment.persistence.model.AdminModeFeatureIntegration;

import java.util.List;

/**
 * @author rupak ON 2020/06/03-10:08 PM
 */
public class IntegrationAdminModeFeatureUtils {
    public static void parseToDeletedAdminModeFeatureIntegration(AdminModeFeatureIntegration adminModeFeatureIntegration, DeleteRequestDTO deleteRequestDTO) {

        adminModeFeatureIntegration.setStatus(deleteRequestDTO.getStatus());
        adminModeFeatureIntegration.setRemarks(deleteRequestDTO.getRemarks());


    }

    public static void parseToDeletedAdminModeApiFeatureIntegration(List<AdminModeApiFeatureIntegration> adminModeApiFeatureIntegrationList) {
    }
}
