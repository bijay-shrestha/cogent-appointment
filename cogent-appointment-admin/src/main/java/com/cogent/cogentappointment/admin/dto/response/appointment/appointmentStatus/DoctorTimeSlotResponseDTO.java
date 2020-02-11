package com.cogent.cogentappointment.admin.dto.response.appointment.appointmentStatus;

import com.cogent.cogentappointment.persistence.enums.Gender;
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

    private Gender gender;

    private String patientName;
}
