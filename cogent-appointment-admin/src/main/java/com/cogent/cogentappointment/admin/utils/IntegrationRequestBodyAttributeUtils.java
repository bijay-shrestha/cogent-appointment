package com.cogent.cogentappointment.admin.utils;

import com.cogent.cogentappointment.admin.dto.commons.DeleteRequestDTO;
import com.cogent.cogentappointment.persistence.model.ApiFeatureIntegrationRequestBodyParameters;
import com.cogent.cogentappointment.persistence.model.ApiIntegrationRequestBodyParameters;

import java.util.ArrayList;
import java.util.List;

import static com.cogent.cogentappointment.admin.constants.StatusConstants.ACTIVE;

/**
 * @author rupak ON 2020/06/02-12:22 PM
 */
public class IntegrationRequestBodyAttributeUtils {


    public static List<ApiFeatureIntegrationRequestBodyParameters> parseToClientApiFeatureRequestBodyParameters
            (Long featureId, List<ApiIntegrationRequestBodyParameters> bodyParametersList) {

        List<ApiFeatureIntegrationRequestBodyParameters> apiFeatureIntegrationRequestBodyParameters
                = new ArrayList<>();
        bodyParametersList.forEach(requestBody -> {

            ApiFeatureIntegrationRequestBodyParameters requestBodyParameters =
                    new ApiFeatureIntegrationRequestBodyParameters();
            requestBodyParameters.setFeatureId(featureId);
            requestBodyParameters.setRequestBodyParametersId(requestBody);
            requestBodyParameters.setStatus(ACTIVE);

            apiFeatureIntegrationRequestBodyParameters.add(requestBodyParameters);


        });


        return apiFeatureIntegrationRequestBodyParameters;

    }

    public static void parseToDeletedApiFeatureIntegrationRequestBodyParameters
            (List<ApiFeatureIntegrationRequestBodyParameters> requestBodyParameters,
             DeleteRequestDTO deleteRequestDTO) {

        requestBodyParameters.forEach(bodyParamters -> {
            bodyParamters.setStatus(deleteRequestDTO.getStatus());
            bodyParamters.setRemarks(deleteRequestDTO.getRemarks());

        });

    }
}
