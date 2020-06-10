package com.cogent.cogentappointment.admin.dto.response.integrationClient;

import lombok.*;

import java.io.Serializable;
import java.util.List;

/**
 * @author rupak on 2020-05-20
 */
@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class ClientFeatureIntegrationResponseDTO implements Serializable {

    private String featureCode;

    private String requestMethod;

    private String url;

    private String requestBody;

    private List<ApiRequestHeaderResponseDTO> headers;

    private ApiQueryParametersResponseDTO queryParameters;


}
