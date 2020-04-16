package com.cogent.cogentappointment.client.dto.response.appointment.log;

import lombok.*;

import java.io.Serializable;
import java.util.List;
import java.util.Map;

/**
 * @author Rupak
 */
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class AppointmentLogResponseDTO implements Serializable {

    private List<AppointmentLogDTO> appointmentLogs;

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

    private int totalItems;
}
