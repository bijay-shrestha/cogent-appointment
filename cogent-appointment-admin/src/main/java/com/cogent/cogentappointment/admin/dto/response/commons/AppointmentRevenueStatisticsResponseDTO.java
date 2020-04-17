package com.cogent.cogentappointment.admin.dto.response.commons;

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

    private Double totalAmount;

    private Double totalAmountExcludingBooked;

    private Double bookedAmount;

    private Double checkedInAmount;

    private Double refundedAmount;

    private Double cancelAmount;

    private Double revenueFromRefundedAmount;

    private Long bookedAppointmentsCount;

    private Long checkedInAppointmentsCount;

    private Long refundedAppointmentsCount;

    private Long cancelAppointmentsCount;

    private Long revenueFromRefundedAppointmentsCount;
}
