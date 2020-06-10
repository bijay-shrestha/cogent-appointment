package com.cogent.cogentthirdpartyconnector.response.integrationBackend;

import lombok.*;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;

import java.io.Serializable;
import java.util.Map;

/**
 * @author rupak ON 2020/06/10-11:31 AM
 */
@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class BackendIntegrationHospitalApiInfo implements Serializable {

    private String apiUri;

    private HttpHeaders httpHeaders;

    private Map<String, String> queryParameters;

    private String httpMethod;




}
