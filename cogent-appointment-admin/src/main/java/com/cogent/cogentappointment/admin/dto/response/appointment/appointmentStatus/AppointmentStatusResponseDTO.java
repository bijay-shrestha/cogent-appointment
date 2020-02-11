package com.cogent.cogentappointment.admin.dto.response.appointment.appointmentStatus;

import com.cogent.cogentappointment.persistence.enums.Gender;
import lombok.*;

import java.io.Serializable;
import java.time.LocalDate;

/**
 * @author smriti ON 16/12/2019
 */
@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class AppointmentStatusResponseDTO implements Serializable {

    private LocalDate date;

    private Long doctorId;

    private Long specializationId;

    private String appointmentTimeDetails;

    private String appointmentNumber;

    private String mobileNumber;

    private String age;

    private Gender gender;

    private String patientName;
}
