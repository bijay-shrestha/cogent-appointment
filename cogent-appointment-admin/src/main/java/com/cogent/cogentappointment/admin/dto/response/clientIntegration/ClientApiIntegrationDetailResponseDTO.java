package com.cogent.cogentappointment.admin.dto.response.clientIntegration;

import lombok.*;

import java.io.Serializable;
import java.util.Map;

/**
 * @author rupak on 2020-05-25
 */
@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class ClientApiIntegrationDetailResponseDTO  implements Serializable {

    private String hospitalName;

    private String featureCode;

    private String requestMethod;

    private String url;

    private String requestBody;

    private Map<String,String> headers;

    private Map<String,String> queryParameters;

}
