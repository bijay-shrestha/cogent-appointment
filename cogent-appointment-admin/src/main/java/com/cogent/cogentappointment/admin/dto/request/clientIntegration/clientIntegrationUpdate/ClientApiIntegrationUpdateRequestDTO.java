package com.cogent.cogentappointment.admin.dto.request.clientIntegration.clientIntegrationUpdate;

import com.cogent.cogentappointment.admin.dto.request.clientIntegration.ClientApiHeadersRequestDTO;
import com.cogent.cogentappointment.admin.dto.request.clientIntegration.ClientApiQueryParametersRequestDTO;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.util.List;

/**
 * @author rupak on 2020-05-22
 */
@Getter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class ClientApiIntegrationUpdateRequestDTO implements Serializable {

    private Long clientFeatureIntegrationId;

    private Long requestMethodId;

    private String apiUrl;

    private List<ClientApiHeadersRequestDTO> clientApiRequestHeaders;

    private List<ClientApiQueryParametersRequestDTO> parametersRequestDTOS;

    private String requestBodyAttrribute;

}
