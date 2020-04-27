package com.cogent.cogentappointment.client.dto.response.appointment.log;

import com.cogent.cogentappointment.client.dto.response.commons.AppointmentRevenueStatisticsResponseDTO;
import lombok.*;

import java.io.Serializable;
import java.util.List;

/**
 * @author Sauravi Thapa ON 4/19/20
 */
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class TransactionLogResponseDTO implements Serializable {

    private List<TransactionLogDTO> transactionLogs;

    private AppointmentRevenueStatisticsResponseDTO appointmentStatistics;

    private int totalItems;
}
