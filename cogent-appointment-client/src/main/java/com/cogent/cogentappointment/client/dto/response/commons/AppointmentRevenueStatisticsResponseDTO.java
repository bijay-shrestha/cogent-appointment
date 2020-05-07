package com.cogent.cogentappointment.client.dto.response.commons;

import lombok.*;

import java.io.Serializable;

/**
 * @author smriti on 16/04/20
 */
@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class AppointmentRevenueStatisticsResponseDTO implements Serializable {

    private Long bookedAppointmentsCount;

    private Double bookedAmount;

    private Long checkedInAppointmentsCount;

    private Double checkedInAmount;

    private Long refundedAppointmentsCount;

    private Double refundedAmount;

    private Long cancelAppointmentsCount;

    private Double cancelAmount;

    private Long revenueFromRefundedAppointmentsCount;

    private Double revenueFromRefundedAmount;

    private Long followUpCount;

    private Double followUpAmount;

    private Double totalAmount;

    private Double totalAmountExcludingBooked;

}
