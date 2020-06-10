package com.cogent.cogentappointment.client.dto.response.clientIntegration;

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
public class FeatureIntegrationResponse implements Serializable {

    private Long apiIntegrationFormatId;

    private String integrationChannelCode;

    private Long featureId;

    private String featureCode;

    private String url;

    private String requestMethod;
}
