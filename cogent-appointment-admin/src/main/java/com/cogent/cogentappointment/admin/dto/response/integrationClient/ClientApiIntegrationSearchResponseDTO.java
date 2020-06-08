package com.cogent.cogentappointment.admin.dto.response.integrationClient;

import lombok.*;

/**
 * @author rupak on 2020-05-25
 */
@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class ClientApiIntegrationSearchResponseDTO {

    private Long id;

    private String featureName;

    private String featureCode;

    private String requestMethod;

    private String integrationChannel;

    private String url;

    private String hospitalName;

}
