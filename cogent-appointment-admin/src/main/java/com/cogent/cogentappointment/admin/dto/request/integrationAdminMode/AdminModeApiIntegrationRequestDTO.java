package com.cogent.cogentappointment.admin.dto.request.integrationAdminMode;

import com.cogent.cogentappointment.admin.dto.request.integrationClient.ClientApiHeadersRequestDTO;
import com.cogent.cogentappointment.admin.dto.request.integrationClient.ClientApiQueryParametersRequestDTO;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
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

    @NotNull(message = "Client cannot be null")
    private Long hospitalId;

    @NotNull(message = "Appointment Mode cannot be null")
    private Long appointmentModeId;

    @NotNull(message = "Api Integration Type cannot be null")
    private Long apiIntegrationTypeId;

    @NotNull(message = "Integration Channel cannot be null")
    private Long integrationChannelId;

    @NotNull(message = "Feature Type cannot be null")
    private Long featureTypeId;

    private Long requestMethodId;

    @NotNull(message = "API Url cannot be null")
    @NotBlank(message = "API Url cannot be blank")
    @NotEmpty(message = "API Url cannot be empty")
    private String apiUrl;

    private List<ClientApiHeadersRequestDTO> clientApiRequestHeaders;

    private List<ClientApiQueryParametersRequestDTO> parametersRequestDTOS;
}