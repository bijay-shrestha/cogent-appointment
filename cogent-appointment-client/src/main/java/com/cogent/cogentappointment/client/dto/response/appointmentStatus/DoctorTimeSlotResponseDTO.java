package com.cogent.cogentappointment.client.dto.response.appointmentStatus;

import lombok.*;

import java.io.Serializable;

/**
 * @author smriti ON 17/12/2019
 */
@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class DoctorTimeSlotResponseDTO implements Serializable {

    private String appointmentTime;

    private String status;

    private String appointmentNumber;

    private String mobileNumber;

    private String age;

    private String gender;

    private String patientName;
}
