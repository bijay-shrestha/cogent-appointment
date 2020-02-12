package com.cogent.cogentappointment.admin.dto.response.reschedule;

import com.cogent.cogentappointment.admin.dto.response.appointment.AppointmentLogDTO;
import lombok.*;

import java.util.List;

/**
 * @author Rupak
 */
@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class AppointmentRescheduleLogResponseDTO {

    private List<AppointmentRescheduleLogDTO> appointmentRescheduleLogDTOS;

    private Double totalAmount;

    private int totalItems;
}
