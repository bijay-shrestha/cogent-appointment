package com.cogent.cogentappointment.esewa.dto.response.reschedule;

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
