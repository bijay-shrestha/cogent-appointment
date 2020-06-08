package com.cogent.cogentappointment.admin.dto.request.integrationAdminMode;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.io.Serializable;

/**
 * @author rupak ON 2020/06/03-9:36 AM
 */
@Getter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class AdminModeApiIntegrationSearchRequestDTO implements Serializable {

    private Long requestMethodId;

    private Long apiIntegrationTypeId;

    private Long appointmentModeId;

    private Long companyId;

    private Long featureTypeId;

    private String url;

}
