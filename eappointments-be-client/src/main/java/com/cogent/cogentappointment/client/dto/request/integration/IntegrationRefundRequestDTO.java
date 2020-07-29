package com.cogent.cogentappointment.client.dto.request.integration;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotNull;
import java.io.Serializable;

/**
 * @author rupak ON 2020/06/18-2:38 PM
 */
@Getter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class IntegrationRefundRequestDTO implements Serializable {

    @NotNull
    private Long appointmentId;

    @NotNull
    private Long appointmentModeId;

    //FULL_REFUND, PARTIAL_REFUND, AMIBGIUOS
    private String status;

    private String featureCode;

    private String integrationChannelCode;

    private String remarks;


}
