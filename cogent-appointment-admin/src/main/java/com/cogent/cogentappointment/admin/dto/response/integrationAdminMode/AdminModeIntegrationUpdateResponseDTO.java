package com.cogent.cogentappointment.admin.dto.response.integrationAdminMode;

import com.cogent.cogentappointment.admin.dto.response.integrationClient.clientIntegrationUpdate.ApiQueryParametersUpdateResponseDTO;
import com.cogent.cogentappointment.admin.dto.response.integrationClient.clientIntegrationUpdate.ApiRequestHeaderUpdateResponseDTO;
import lombok.*;

import java.io.Serializable;
import java.util.List;

/**
 * @author rupak ON 2020/06/05-7:17 AM
 */
@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class AdminModeIntegrationUpdateResponseDTO implements Serializable {


    private String appointmentMode;

    private Long appointmentModeId;

    private Long featureId;

    private String featureName;

    private Long requestMethodId;

    private String requestMethodName;

    private Long integrationChannelId;

    private String integrationChannel;

    private Long integrationTypeId;

    private String integrationType;

    private String url;

    private List<ApiRequestHeaderUpdateResponseDTO> headers;

    private List<ApiQueryParametersUpdateResponseDTO> queryParameters;




}
