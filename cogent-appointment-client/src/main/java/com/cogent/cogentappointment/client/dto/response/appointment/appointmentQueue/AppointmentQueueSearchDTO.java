package com.cogent.cogentappointment.client.dto.response.appointment.appointmentQueue;

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
public class AppointmentQueueSearchDTO {

    private List<AppointmentQueueDTO> appointmentQueueByTimeDTOList;

    private int totalItems;

}
