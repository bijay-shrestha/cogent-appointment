package com.cogent.cogentappointment.admin.dto.response.appointment.appointmentQueue;

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
public class AppointmentQueueSearchByTimeDTO implements Serializable {

    private Long appointmentTime;

    private List<AppointmentByTimeResponseDTO> appointmentByTimeResponseDTOList;


}
