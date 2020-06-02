package com.cogent.cogentappointment.admin.dto.request.integrationAdminMode;

import com.cogent.cogentappointment.admin.dto.request.integrationClient.ClientApiHeadersRequestDTO;
import com.cogent.cogentappointment.admin.dto.request.integrationClient.ClientApiQueryParametersRequestDTO;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.util.List;

/**
 * @author rupak on 2020-06-02
 */
@Getter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class AdminModeApiIntegrationRequestDTO implements Serializable {

    private Long appointmentModeId;

    private Long apiIntegrationTypeId;

    private Long integrationChannelId;

    private Long featureTypeId;

    private Long requestMethodId;

    private String apiUrl;

    private List<Long> requestBodyAttrributeId;

    private List<ClientApiHeadersRequestDTO> clientApiRequestHeaders;

    private List<ClientApiQueryParametersRequestDTO> parametersRequestDTOS;
}