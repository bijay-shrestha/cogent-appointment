package com.cogent.cogentappointment.esewa.dto.response.appointment.appointmentQueue;

import lombok.*;

/**
 * @author Rupak
 */
@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class AppointmentByTimeResponseDTO {

    private String appointmentTime;

    private String doctorName;

    private String specializationName;

    private String patientName;

    private String patientMobileNumber;

    private String doctorAvatar;

}
