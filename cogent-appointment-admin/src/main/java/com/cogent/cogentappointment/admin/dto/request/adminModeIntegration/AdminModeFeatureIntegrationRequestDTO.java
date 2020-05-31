package com.cogent.cogentappointment.admin.dto.request.adminModeIntegration;

import com.cogent.cogentappointment.admin.dto.request.integrationClient.ClientApiHeadersRequestDTO;
import com.cogent.cogentappointment.admin.dto.request.integrationClient.ClientApiQueryParametersRequestDTO;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.util.List;

/**
 * @author rupak on 2020-05-21
 */
@Getter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class AdminModeFeatureIntegrationRequestDTO implements Serializable {

    private Long appointmentModeId;

    private Long featureTypeId;

    private Long requestMethodId;

    private String apiUrl;

    private List<ClientApiHeadersRequestDTO> clientApiRequestHeaders;

    private List<ClientApiQueryParametersRequestDTO> parametersRequestDTOS;

    private String requestBodyAttrribute;


}
