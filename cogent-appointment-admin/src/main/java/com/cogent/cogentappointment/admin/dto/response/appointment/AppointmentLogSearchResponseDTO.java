package com.cogent.cogentappointment.admin.dto.response.appointment;

import lombok.*;

import java.util.List;

/**
 * @author Rupak
 */

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class AppointmentLogSearchResponseDTO {

    private List<AppointmentLogResponseDTO> appointmentLogSearchDTOList;

    private Double totalAmount;

    private int totalItems;
}
