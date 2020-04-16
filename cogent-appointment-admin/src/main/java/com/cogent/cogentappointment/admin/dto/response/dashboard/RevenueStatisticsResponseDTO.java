package com.cogent.cogentappointment.admin.dto.response.dashboard;

import com.cogent.cogentappointment.admin.dto.response.commons.AppointmentStatisticsResponseDTO;
import lombok.*;

import java.io.Serializable;

/**
 * @author Sauravi Thapa २०/२/१०
 */
@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class RevenueStatisticsResponseDTO implements Serializable {

    private String fiscalYear;

    private Double amount;

    private Double growthPercent;

    private Character filterType;

    private AppointmentStatisticsResponseDTO appointmentStatistics;
}
