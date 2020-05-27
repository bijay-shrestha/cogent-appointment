package com.cogent.cogentappointment.admin.dto.request.clientIntegration.clientIntegrationUpdate;

import lombok.*;

import java.io.Serializable;
import java.util.List;

/**
 * @author rupak on 2020-05-22
 */
@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class ClientApiIntegrationUpdateRequestDTO implements Serializable {

    private Long clientFeatureIntegrationId;

    private Long requestMethodId;

    private String apiUrl;

    private List<ClientApiRequestHeadersUpdateRequestDTO> clientApiRequestHeaders;

    private List<ClientApiQueryParamtersUpdateRequestDTO> queryParametersRequestDTOS;

    private String requestBodyAttrribute;

}
