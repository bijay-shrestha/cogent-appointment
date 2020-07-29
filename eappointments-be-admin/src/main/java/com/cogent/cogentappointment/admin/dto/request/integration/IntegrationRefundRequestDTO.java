package com.cogent.cogentappointment.admin.dto.request.integration;

import lombok.*;

import javax.validation.constraints.NotNull;
import java.io.Serializable;

/**
 * @author rupak ON 2020/06/18-2:38 PM
 */
@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class IntegrationRefundRequestDTO implements Serializable {

    @NotNull
    private Long appointmentId;

    @NotNull
    private Long hospitalId;

    @NotNull
    private Long appointmentModeId;

    //FULL_REFUND, PARTIAL_REFUND, AMIBIGIUOS
    private String status;

    private String featureCode;

    private String integrationChannelCode;

    private String remarks;

}
