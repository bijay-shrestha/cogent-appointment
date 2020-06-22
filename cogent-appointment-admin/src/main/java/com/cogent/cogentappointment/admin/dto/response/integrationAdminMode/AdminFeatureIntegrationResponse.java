package com.cogent.cogentappointment.admin.dto.response.integrationAdminMode;

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
public class AdminFeatureIntegrationResponse implements Serializable {

    private Long apiIntegrationFormatId;

    private Long hospitalId;

    private Long appointmentModeId;

    private String integrationChannelCode;

    private Long featureId;

    private String featureCode;

    private String url;

    private String requestMethod;
}
