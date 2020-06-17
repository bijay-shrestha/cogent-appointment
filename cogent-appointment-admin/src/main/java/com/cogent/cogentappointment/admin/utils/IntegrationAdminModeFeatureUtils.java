package com.cogent.cogentappointment.admin.utils;

import com.cogent.cogentappointment.admin.dto.commons.DeleteRequestDTO;
import com.cogent.cogentappointment.admin.dto.request.integrationAdminMode.integrationAdminModeUpdate.AdminModeIntegrationUpdateRequestDTO;
import com.cogent.cogentappointment.admin.dto.request.integrationClient.ClientApiHeadersRequestDTO;
import com.cogent.cogentappointment.admin.dto.request.integrationClient.ClientApiQueryParametersRequestDTO;
import com.cogent.cogentappointment.persistence.model.*;

import java.util.ArrayList;
import java.util.List;

import static com.cogent.cogentappointment.admin.constants.StatusConstants.ACTIVE;
import static com.cogent.cogentappointment.admin.constants.StatusConstants.DELETED;

/**
 * @author rupak ON 2020/06/03-10:08 PM
 */
public class IntegrationAdminModeFeatureUtils {

    public static AdminModeFeatureIntegration parseToAdminModeFeatureIntegration(AppointmentMode appointmentMode,
                                                                                 Long featureTypeId,
                                                                                 IntegrationChannel integrationChannelId,
                                                                                 Hospital hospital) {

        AdminModeFeatureIntegration adminModeFeatureIntegration = new AdminModeFeatureIntegration();
        adminModeFeatureIntegration.setFeatureId(featureTypeId);
        adminModeFeatureIntegration.setAppointmentModeId(appointmentMode);
        adminModeFeatureIntegration.setIntegrationChannelId(integrationChannelId);
        adminModeFeatureIntegration.setHospitalId(hospital);
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

    public static void parseToDeletedAdminModeFeatureIntegration(AdminModeFeatureIntegration adminModeFeatureIntegration,
                                                                 DeleteRequestDTO deleteRequestDTO) {

        adminModeFeatureIntegration.setStatus(deleteRequestDTO.getStatus());
        adminModeFeatureIntegration.setRemarks(deleteRequestDTO.getRemarks());


    }

    public static void parseToDeletedAdminModeApiFeatureIntegration(List<AdminModeApiFeatureIntegration> adminModeApiFeatureIntegrationList) {

        adminModeApiFeatureIntegrationList.forEach(adminModeApiFeatureIntegration -> {
            adminModeApiFeatureIntegration.setStatus(DELETED);
        });
    }

    public static List<AdminModeRequestHeader> parseToAdminModeRequestHeaders(List<ClientApiHeadersRequestDTO> adminModeRequestHeaders,
                                                                              Long id) {

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

    public static void parseToUpdateAdminModeFeatureIntegration(AppointmentMode appointmentMode,
                                                                IntegrationChannel integrationChannel,
                                                                AdminModeIntegrationUpdateRequestDTO requestDTO,
                                                                AdminModeFeatureIntegration adminModeFeatureIntegration,
                                                                Hospital hospital) {

        adminModeFeatureIntegration.setAppointmentModeId(appointmentMode);
        adminModeFeatureIntegration.setFeatureId(requestDTO.getFeatureId());
        adminModeFeatureIntegration.setIntegrationChannelId(integrationChannel);
        adminModeFeatureIntegration.setRemarks(requestDTO.getRemarks());
        adminModeFeatureIntegration.setHospitalId(hospital);
        adminModeFeatureIntegration.setStatus(requestDTO.getStatus());


    }

    public static void parseToDeletedApiRequestHeaders(List<AdminModeRequestHeader> apiRequestHeaderListToDelete) {

        apiRequestHeaderListToDelete.forEach(apiRequestHeader -> {
            apiRequestHeader.setStatus(DELETED);
        });
    }

    public static void parseToDeletedApiQueryParameters(List<AdminModeQueryParameters> apiQueryParameterToDelete) {

        apiQueryParameterToDelete.forEach(apiQueryParamters -> {
            apiQueryParamters.setStatus(DELETED);
        });
    }
}
