package com.cogent.cogentappointment.admin.dto.response.clientIntegration.clientIntegrationUpdate;

import lombok.*;

import java.io.Serializable;
import java.util.List;
import java.util.Map;

/**
 * @author rupak on 2020-05-26
 */
@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class ClientApiIntegrationUpdateResponseDTO implements Serializable {

    private String hospitalName;

    private String featureCode;

    private String requestMethod;

    private String url;

    private String requestBody;

    private List<ApiRequestHeaderUpdateResponseDTO> headers;

    private List<ApiQueryParametersUpdateResponseDTO> queryParameters;

}
