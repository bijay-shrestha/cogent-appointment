package com.cogent.cogentappointment.client.dto.request.appointment.refund;

import lombok.Getter;
import lombok.Setter;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import java.io.Serializable;

/**
 * @author smriti ON 08/02/2020
 */
@Getter
@Setter
public class AppointmentRefundRejectDTO implements Serializable {

    @NotNull
    private Long appointmentId;

    @NotNull
    private Long appointmentModeId;

    //FULL_REFUND, PARTIAL_REFUND, AMIBGIUOS
    private String status;

    private String featureCode;

    private String integrationChannelCode;

    @NotNull
    @NotEmpty
    private String remarks;
}
