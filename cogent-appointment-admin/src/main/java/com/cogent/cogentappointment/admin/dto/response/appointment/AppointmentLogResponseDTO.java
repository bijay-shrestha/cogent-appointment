package com.cogent.cogentappointment.admin.dto.response.appointment;

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

    private List<AppointmentLogDTO> appointmentLogSearchDTOList;

    private Double totalAmount;

    private int totalItems;
}
