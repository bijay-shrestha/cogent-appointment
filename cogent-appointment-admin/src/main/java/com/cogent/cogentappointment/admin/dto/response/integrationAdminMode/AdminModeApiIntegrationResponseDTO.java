package com.cogent.cogentappointment.admin.dto.response.integrationAdminMode;

import lombok.*;

import java.io.Serializable;
import java.util.Date;

/**
 * @author rupak ON 2020/06/04-11:58 AM
 */
@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class AdminModeApiIntegrationResponseDTO implements Serializable {

    private Long featureId;

    private Long appointmentModeId;

    private String appointmentModeName;

    private String featureCode;

    private Long requestMethodId;

    private String requestMethodName;

    private Long integrationChannelId;

    private String integrationChannel;

    private Long integrationTypeId;

    private String integrationType;

    private String url;

    private String createdBy;

    private Date createdDate;

    private String lastModifiedBy;

    private Date lastModifiedDate;

}
