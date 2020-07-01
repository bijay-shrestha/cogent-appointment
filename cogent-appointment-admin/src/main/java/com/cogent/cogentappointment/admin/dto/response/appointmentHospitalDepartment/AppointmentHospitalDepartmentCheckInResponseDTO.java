package com.cogent.cogentappointment.admin.dto.response.appointmentHospitalDepartment;

import com.cogent.cogentappointment.persistence.enums.Gender;
import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.*;

import java.io.Serializable;
import java.util.Date;

/**
 * @author smriti on 14/06/20
 */
@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class AppointmentHospitalDepartmentCheckInResponseDTO implements Serializable {

    private Long appointmentId;

    private String hospitalName;

    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "MMM dd,YYYY", timezone = "Asia/Kathmandu")
    private Date appointmentDate;

    private String appointmentTime;

    private String appointmentNumber;

    private String transactionNumber;

    private Double appointmentAmount;

    private String patientName;

    private String mobileNumber;

    private Gender gender;

    private String age;

    private Character isRegistered;

    private String registrationNumber;

    private String hospitalNumber;

    /*Province, District, VDC/Municipality, Ward Number*/
    private String address;

    private String hospitalDepartmentName;

    private String roomNumber;

    private int totalItems;
}
