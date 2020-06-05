package com.cogent.cogentappointment.admin.dto.request.integrationAdminMode.integrationAdminModeUpdate;

import com.cogent.cogentappointment.admin.dto.request.integrationClient.clientIntegrationUpdate.ClientApiQueryParamtersUpdateRequestDTO;
import com.cogent.cogentappointment.admin.dto.request.integrationClient.clientIntegrationUpdate.ClientApiRequestHeadersUpdateRequestDTO;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.util.List;

/**
 * @author rupak ON 2020/06/05-6:41 AM
 */
@Getter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class AdminModeIntegrationUpdateRequestDTO implements Serializable {

    private Long adminModeIntegrationId;

    private Long appointmentModeId;

    private Long featureId;

    private Long requestMethodId;

    private Long integrationChannelId;

    private Long integrationTypeId;

    private String apiUrl;

    private List<ClientApiRequestHeadersUpdateRequestDTO> clientApiRequestHeaders;

    private List<ClientApiQueryParamtersUpdateRequestDTO> queryParametersRequestDTOS;

    private String requestBodyAttrribute;

    private String remarks;
}
