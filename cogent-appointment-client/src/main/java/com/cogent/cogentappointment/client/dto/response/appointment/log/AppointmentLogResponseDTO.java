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

    private Map<Long,Double> bookedAmount;

    private Map<Long,Double> checkedInAmount;

    private Map<Long,Double> refundedAmount;

    private Map<Long,Double> cancelAmount;

    private Map<Long,Double> revenueFromRefundedAmount;

    private int totalItems;
}
