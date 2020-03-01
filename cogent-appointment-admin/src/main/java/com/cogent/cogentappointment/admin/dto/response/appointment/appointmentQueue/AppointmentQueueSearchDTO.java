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
public class AppointmentQueueSearchDTO implements Serializable {

    private List<AppointmentQueueDTO> appointmentQueueByTimeDTOList;

    private int totalItems;

}
