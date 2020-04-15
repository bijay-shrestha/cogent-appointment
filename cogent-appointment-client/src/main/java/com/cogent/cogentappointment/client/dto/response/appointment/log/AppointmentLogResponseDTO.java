package com.cogent.cogentappointment.client.dto.response.appointment.log;

import lombok.*;

import java.io.Serializable;
import java.util.List;

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

    private String bookedAmount;

    private String checkedInAmount;

    private String refundedAmount;

    private String cancelAmount;

    private String revenueFromRefundedAmount;

    private int totalItems;
}
