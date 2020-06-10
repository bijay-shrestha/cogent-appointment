package com.cogent.cogentappointment.admin.dto.response.integrationAdminMode;

import lombok.*;

import java.io.Serializable;

/**
 * @author rupak ON 2020/06/03-9:48 AM
 */
@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class AdminModeIntegrationSearchResponseDTO implements Serializable {

    private Long id;

    private String appointmentMode;

    private String featureName;

    private String featureCode;

    private String requestMethod;

    private String integrationChannel;

    private String url;

    private String companyName;

}
