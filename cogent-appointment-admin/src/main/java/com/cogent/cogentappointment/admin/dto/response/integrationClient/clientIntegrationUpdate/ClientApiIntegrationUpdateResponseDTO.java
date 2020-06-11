package com.cogent.cogentappointment.admin.dto.response.integrationClient.clientIntegrationUpdate;

import lombok.*;

import java.io.Serializable;
import java.util.List;

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

    private String featureName;

    private Long requestMethodId;

    private String requestMethodName;

    private Long integrationChannelId;

    private String integrationChannel;

    private Long integrationTypeId;

    private String integrationType;

    private String url;

    private List<ApiRequestHeaderUpdateResponseDTO> headers;

    private List<ApiQueryParametersUpdateResponseDTO> queryParameters;

}
