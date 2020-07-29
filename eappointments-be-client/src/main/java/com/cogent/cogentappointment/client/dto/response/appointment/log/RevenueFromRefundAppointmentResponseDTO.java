package com.cogent.cogentappointment.client.dto.response.appointment.log;

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
