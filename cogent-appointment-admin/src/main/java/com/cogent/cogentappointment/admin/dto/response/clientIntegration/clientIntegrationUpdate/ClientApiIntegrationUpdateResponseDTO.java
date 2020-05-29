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

    private Long featureId;

    private String featureCode;

    private Long requestMethodId;

    private String requestMethodName;

    private String url;

    private List<ApiRequestHeaderUpdateResponseDTO> headers;

    private List<ApiQueryParametersUpdateResponseDTO> queryParameters;

}
