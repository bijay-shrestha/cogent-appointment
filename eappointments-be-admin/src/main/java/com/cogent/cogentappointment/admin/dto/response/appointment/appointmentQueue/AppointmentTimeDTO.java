package com.cogent.cogentappointment.admin.dto.response.appointment.appointmentQueue;

import lombok.*;

import java.io.Serializable;

/**
 * @author Rupak
 */
@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class AppointmentTimeDTO implements Serializable {

    private String appointmentTime;
}
