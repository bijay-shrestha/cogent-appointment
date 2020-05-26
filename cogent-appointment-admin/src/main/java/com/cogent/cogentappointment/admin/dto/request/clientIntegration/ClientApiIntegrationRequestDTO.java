package com.cogent.cogentappointment.admin.dto.request.clientIntegration;

import lombok.*;

import java.io.Serializable;
import java.util.List;

/**
 * @author rupak on 2020-05-19
 */
@Getter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class ClientApiIntegrationRequestDTO implements Serializable {

    private Long hospitalId;

    private Long apiIntegrationTypeId;

    private Long featureTypeId;

    private Long requestMethodId;

    private String apiUrl;

    private List<ClientApiHeadersRequestDTO> clientApiRequestHeaders;

    private List<ClientApiQueryParametersRequestDTO> parametersRequestDTOS;

    private String requestBodyAttrribute;
}