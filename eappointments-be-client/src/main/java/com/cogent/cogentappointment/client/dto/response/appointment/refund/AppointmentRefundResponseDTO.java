package com.cogent.cogentappointment.client.dto.response.appointment.refund;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;
import java.util.List;

/**
 * @author smriti ON 08/02/2020
 */
@Getter
@Setter
@Builder
public class AppointmentRefundResponseDTO implements Serializable {

    private Double totalRefundAmount;

    private int totalItems;

    private List<AppointmentRefundDTO> refundAppointments;
}
