package com.cogent.cogentappointment.admin.dto.response.reschedule;

import lombok.*;

import java.io.Serializable;
import java.util.List;

/**
 * @author Rupak
 */
@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class AppointmentRescheduleLogResponseDTO implements Serializable {

    private List<AppointmentRescheduleLogDTO> appointmentRescheduleLogDTOS;

    private Double totalAmount;

    private int totalItems;
}
