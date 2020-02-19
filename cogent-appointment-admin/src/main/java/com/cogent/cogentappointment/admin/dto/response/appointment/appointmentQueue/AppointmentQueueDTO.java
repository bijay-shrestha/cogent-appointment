package com.cogent.cogentappointment.admin.dto.response.appointment.appointmentQueue;

import lombok.*;

/**
 * @author Rupak
 */
@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class AppointmentQueueDTO {

    private String appointmentTime;

    private String doctorName;

    private String specializationName;

    private String patientName;

    private String patientMobileNumber;

    private String doctorAvatar;


}
