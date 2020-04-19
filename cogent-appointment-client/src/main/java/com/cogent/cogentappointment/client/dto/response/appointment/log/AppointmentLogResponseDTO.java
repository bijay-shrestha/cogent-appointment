package com.cogent.cogentappointment.client.dto.response.appointment.log;

import com.cogent.cogentappointment.client.dto.response.commons.AppointmentRevenueStatisticsResponseDTO;
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

    private AppointmentRevenueStatisticsResponseDTO appointmentStatistics;

    private int totalItems;
}
