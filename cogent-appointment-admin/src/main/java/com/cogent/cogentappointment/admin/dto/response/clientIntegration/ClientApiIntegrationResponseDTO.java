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

    private String hospitalName;

    private String featureCode;

    private Long requestMethodId;

    private String requestMethodName;

    private Long integrationChannelId;

    private String integrationChannel;

    private Long integrationTypeId;

    private String integrationType;

    private String url;
}
