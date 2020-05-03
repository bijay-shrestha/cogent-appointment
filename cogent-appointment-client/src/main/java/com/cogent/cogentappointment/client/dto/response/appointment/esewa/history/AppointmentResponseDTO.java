package com.cogent.cogentappointment.client.dto.response.appointment.esewa.history;

import com.cogent.cogentappointment.persistence.enums.Gender;
import lombok.*;

import java.io.Serializable;
import java.util.Date;

/**
 * @author smriti on 17/02/20
 */
@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class AppointmentResponseDTO implements Serializable {

    private Long appointmentId;

    private String hospitalName;

    private String patientName;

    private String mobileNumber;

    private String age;

    private Gender gender;

    private Long doctorId;

    private String doctorName;

    private Long specializationId;

    private String specializationName;

    private String appointmentNumber;

    private Date appointmentDate;

    private String appointmentTime;

    private Double appointmentAmount;
}
