package com.cogent.cogentappointment.admin.dto.response.clientIntegration;

import lombok.*;

import java.io.Serializable;

/**
 * @author rupak on 2020-05-25
 */
@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class ClientApiIntegrationResponseDTO implements Serializable {

    private Long featureId;

    private String featureCode;

    private String requestMethod;

    private String url;

    private String requestBody;
}
