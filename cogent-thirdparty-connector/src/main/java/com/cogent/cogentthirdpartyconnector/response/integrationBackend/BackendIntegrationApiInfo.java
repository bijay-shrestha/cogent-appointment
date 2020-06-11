package com.cogent.cogentthirdpartyconnector.response.integrationBackend;

import lombok.*;
import org.springframework.http.HttpHeaders;

import java.io.Serializable;
import java.util.List;
import java.util.Map;

/**
 * @author rupak ON 2020/06/10-11:31 AM
 */
@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class BackendIntegrationApiInfo implements Serializable {

    private String apiUri;

    private HttpHeaders httpHeaders;

    private Map<String, String> queryParameters;

    private List<String> requestBody;

    private String httpMethod;

}
