package com.cogent.cogentappointment.admin.dto.response.appointment.appointmentPatient;

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
public class AppointmentPatientByTimeResponseDTO implements Serializable {

    private String appointmentNumber;

    private String appointmentTime;

    private String patientName;

    private String patientMobileNumber;

    private String patientAddress;

}
