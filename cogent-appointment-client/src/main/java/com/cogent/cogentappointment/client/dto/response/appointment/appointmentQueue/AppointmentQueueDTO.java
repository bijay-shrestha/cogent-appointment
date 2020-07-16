package com.cogent.cogentappointment.client.dto.response.appointment.appointmentQueue;

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
public class AppointmentQueueDTO implements Serializable {

    private String appointmentTime;

    private String doctorName;

    private String specializationName;

    private String patientName;

    private String patientMobileNumber;

    private String doctorAvatar;

    private String hospitalDepartmentName;

    private String roomNumber;

    private int totalItems;
}
