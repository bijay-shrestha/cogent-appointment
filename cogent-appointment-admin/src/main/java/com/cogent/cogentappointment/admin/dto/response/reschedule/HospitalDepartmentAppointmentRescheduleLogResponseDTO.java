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
public class HospitalDepartmentAppointmentRescheduleLogResponseDTO implements Serializable {

    private List<HospitalDepartmentAppointmentRescheduleLogDTO> appointmentRescheduleLogDTOS;

    private Double totalAmount;

    private int totalItems;
}
