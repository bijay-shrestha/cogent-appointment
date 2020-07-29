package com.cogent.cogentappointment.admin.dto.response.appointment.appointmentLog;

import lombok.*;

import java.io.Serializable;

/**
 * @author smriti on 28/04/20
 */
@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class RevenueFromRefundAppointmentResponseDTO implements Serializable{

    private Long revenueFromRefundCount;

    private Double revenueFromRefundAmount;

    private Long followUpCount;

    private Double followUpAmount;
}
