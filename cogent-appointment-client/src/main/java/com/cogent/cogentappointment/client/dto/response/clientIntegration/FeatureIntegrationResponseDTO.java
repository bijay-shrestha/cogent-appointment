package com.cogent.cogentappointment.client.dto.response.clientIntegration;

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

    private String requestMethod;

    private String url;

    private String requestBody;

    private Map<String,String> headers;

    private Map<String,String> queryParameters;


}
