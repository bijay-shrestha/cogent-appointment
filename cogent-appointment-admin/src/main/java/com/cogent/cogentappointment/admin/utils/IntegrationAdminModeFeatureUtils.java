package com.cogent.cogentappointment.admin.utils;

import com.cogent.cogentappointment.admin.dto.commons.DeleteRequestDTO;
import com.cogent.cogentappointment.admin.dto.request.integrationClient.ClientApiHeadersRequestDTO;
import com.cogent.cogentappointment.admin.dto.request.integrationClient.ClientApiQueryParametersRequestDTO;
import com.cogent.cogentappointment.persistence.model.*;

import java.util.ArrayList;
import java.util.List;

import static com.cogent.cogentappointment.admin.constants.StatusConstants.ACTIVE;

/**
 * @author rupak ON 2020/06/03-10:08 PM
 */
public class IntegrationAdminModeFeatureUtils {

    public static AdminModeFeatureIntegration parseToAdminModeFeatureIntegration(AppointmentMode appointmentMode,
                                                                                 Long featureTypeId,
                                                                                 IntegrationChannel integrationChannelId) {

        AdminModeFeatureIntegration adminModeFeatureIntegration = new AdminModeFeatureIntegration();
        adminModeFeatureIntegration.setFeatureId(featureTypeId);
        adminModeFeatureIntegration.setAppointmentModeId(appointmentMode);
        adminModeFeatureIntegration.setIntegrationChannelId(integrationChannelId);
        adminModeFeatureIntegration.setStatus(ACTIVE);

        return adminModeFeatureIntegration;
    }

    public static AdminModeApiFeatureIntegration parseToAdminModeApiFeatureIntegration(
            AdminModeFeatureIntegration adminModeApiFeatureIntegration
            , ApiIntegrationFormat apiIntegrationFormat) {

        AdminModeApiFeatureIntegration modeApiFeatureIntegration = new AdminModeApiFeatureIntegration();
        modeApiFeatureIntegration.setAdminModeFeatureIntegrationId(adminModeApiFeatureIntegration);
        modeApiFeatureIntegration.setApiIntegrationFormatId(apiIntegrationFormat);
        modeApiFeatureIntegration.setStatus(ACTIVE);

        return modeApiFeatureIntegration;

    }

    public static void parseToDeletedAdminModeFeatureIntegration(AdminModeFeatureIntegration adminModeFeatureIntegration, DeleteRequestDTO deleteRequestDTO) {

        adminModeFeatureIntegration.setStatus(deleteRequestDTO.getStatus());
        adminModeFeatureIntegration.setRemarks(deleteRequestDTO.getRemarks());


    }

    public static void parseToDeletedAdminModeApiFeatureIntegration(List<AdminModeApiFeatureIntegration> adminModeApiFeatureIntegrationList) {
    }

    public static List<AdminModeRequestHeader> parseToAdminModeRequestHeaders(List<ClientApiHeadersRequestDTO> adminModeRequestHeaders, Long id) {

        List<AdminModeRequestHeader> adminModeRequestHeaderList = new ArrayList<>();

        adminModeRequestHeaders.forEach(request -> {
            AdminModeRequestHeader adminModeRequestHeader = new AdminModeRequestHeader();
            adminModeRequestHeader.setApiIntegrationFormatId(id);
            adminModeRequestHeader.setKeyName(request.getKeyParam());
            adminModeRequestHeader.setValue(request.getValueParam());
            adminModeRequestHeader.setDescription(request.getDescription());
            adminModeRequestHeader.setStatus(ACTIVE);

            adminModeRequestHeaderList.add(adminModeRequestHeader);
        });

        return adminModeRequestHeaderList;


    }

    public static List<AdminModeQueryParameters> parseToAdminModeQueryParameters(List<ClientApiQueryParametersRequestDTO> parametersRequestDTOS,
                                                                                 Long id) {

        List<AdminModeQueryParameters> adminModeQueryParametersList = new ArrayList<>();

        parametersRequestDTOS.forEach(request -> {
            AdminModeQueryParameters adminModeQueryParameters = new AdminModeQueryParameters();
            adminModeQueryParameters.setApiIntegrationFormatId(id);
            adminModeQueryParameters.setParam(request.getKeyParam());
            adminModeQueryParameters.setValue(request.getValueParam());
            adminModeQueryParameters.setDescription(request.getDescription());
            adminModeQueryParameters.setStatus(ACTIVE);

            adminModeQueryParametersList.add(adminModeQueryParameters);
        });

        return adminModeQueryParametersList;



    }
}
