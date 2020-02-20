package com.cogent.cogentappointment.admin.dto.response.appointment.appointmentQueue;

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
public class AppointmentQueueSearchByTimeDTO {

    private Long appointmentTime;

    private List<AppointmentByTimeResponseDTO> appointmentByTimeResponseDTOList;


}
