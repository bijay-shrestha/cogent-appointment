package com.cogent.cogentappointment.client.dto.response.clientIntegration;

import com.cogent.cogentappointment.client.dto.response.integration.IntegrationRequestBodyAttributeResponse;
import lombok.*;

import java.io.Serializable;
import java.util.List;
import java.util.Map;

/**
 * @author rupak on 2020-05-20
 */
@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class FeatureIntegrationResponseDTO implements Serializable {

    private String featureCode;

    private String integrationChannelCode;

    private ApiInfoResponseDTO apiInfo;

}
