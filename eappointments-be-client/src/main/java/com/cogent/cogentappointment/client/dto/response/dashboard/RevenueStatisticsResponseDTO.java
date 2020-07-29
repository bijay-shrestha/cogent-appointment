package com.cogent.cogentappointment.client.dto.response.dashboard;

import com.cogent.cogentappointment.client.dto.response.commons.AppointmentRevenueStatisticsResponseDTO;
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

    private AppointmentRevenueStatisticsResponseDTO appointmentStatistics;
}
