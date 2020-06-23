package com.cogent.cogentappointment.esewa.dto.response.appointment.history;

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

    private Long hospitalId;

    private String hospitalName;

    private String patientName;

    private String mobileNumber;

    private String age;

    private Gender gender;

    private Long doctorId;

    private String doctorName;

    private String doctorSalutation;

    private Long specializationId;

    private String specializationName;

    private String appointmentNumber;

    private Date appointmentDate;

    private String appointmentTime;

    private Double appointmentAmount;

    private String status;

    private String registrationNumber;

    private String hospitalDepartmentName;

    private String roomNumber;

    private Long hospitalDepartmentRoomInfoId;

    private String appointmentDateInNepali;
}
