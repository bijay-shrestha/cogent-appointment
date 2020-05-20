package com.cogent.cogentappointment.admin.utils;

import com.cogent.cogentappointment.admin.constants.StatusConstants;
import com.cogent.cogentappointment.admin.dto.request.clientIntegration.ApiIntegrationFormatRequestDTO;
import com.cogent.cogentappointment.persistence.model.ApiIntegrationFormat;
import com.cogent.cogentappointment.persistence.model.ClientFeatureIntegration;
import com.cogent.cogentappointment.persistence.model.HttpRequestMethod;
import org.omg.PortableInterceptor.ACTIVE;

/**
 * @author rupak on 2020-05-19
 */
public class IntegrationUtils {
    public static ClientFeatureIntegration parseToClientFeatureIntegration(Long hospitalId, Long featureTypeId) {

        ClientFeatureIntegration clientFeatureIntegration=new ClientFeatureIntegration();
        clientFeatureIntegration.setFeatureId(featureTypeId);
        clientFeatureIntegration.setHospitalId(hospitalId);
        clientFeatureIntegration.setStatus(StatusConstants.ACTIVE);

        return clientFeatureIntegration;
    }

    public static ApiIntegrationFormat parseToClientApiIntegrationFormat(ApiIntegrationFormatRequestDTO requestDTO, HttpRequestMethod httpRequestMethod) {

        ApiIntegrationFormat apiIntegrationFormat=new ApiIntegrationFormat();
        apiIntegrationFormat.setHttpRequestBodyAttributes(requestDTO.getRequestBodyAttrribute());
        apiIntegrationFormat.setHttpRequestMethodId(httpRequestMethod.getId());
        apiIntegrationFormat.setUrl(requestDTO.getApiUrl());
        apiIntegrationFormat.setStatus(StatusConstants.ACTIVE);

        return apiIntegrationFormat;
    }
}
