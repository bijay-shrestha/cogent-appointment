package com.cogent.cogentappointment.admin.dto.response.appointment.appointmentLog;

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

    private BookedAppointmentResponseDTO bookedInfo;

    private CheckedInAppointmentResponseDTO checkedInInfo;

    private CancelledAppointmentResponseDTO cancelledInfo;

    private RefundAppointmentResponseDTO refundInfo;

    private RevenueFromRefundAppointmentResponseDTO revenueFromRefundInfo;

    private Double totalAmount;

    private int totalItems;
}
