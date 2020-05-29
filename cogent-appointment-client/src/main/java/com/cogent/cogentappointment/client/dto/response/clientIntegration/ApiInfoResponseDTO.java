package com.cogent.cogentappointment.client.dto.response.clientIntegration;

import lombok.*;

import java.io.Serializable;
import java.util.Map;

/**
 * @author rupak ON 2020/05/29-4:41 PM
 */
@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class ApiInfoResponseDTO implements Serializable {

    private String url;

    private String requestMethod;

    private Map<String, String> headers;

    private Map<String, String> queryParameters;

    //    private List<IntegrationRequestBodyAttributeResponse> requestBody;
    private String requestBody;

}
