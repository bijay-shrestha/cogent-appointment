package com.cogent.cogentappointment.admin.dto.response.integrationClient;

import lombok.*;

import java.io.Serializable;

/**
 * @author rupak on 2020-05-20
 */
@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class ClientFeatureIntegrationResponse implements Serializable {

    private Long hospitalId;

    private Long apiIntegrationFormatId;

    private String integrationChannelCode;

    private Long featureId;

    private String featureCode;

    private String url;

    private String requestMethod;
}
