package com.cogent.cogentappointment.client.dto.response.appointmentHospitalDepartment;

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
public class AppointmentHospitalDepartmentPendingApprovalResponseDTO implements Serializable {

    private Long appointmentId;

    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "MMM dd,YYYY", timezone = "Asia/Kathmandu")
    private Date appointmentDate;

    private String appointmentNumber;

    private String appointmentTime;

    private String registrationNumber;

    private Long patientId;

    private String patientName;

    private String age;

    private String address;

    private Gender gender;

    private Character isRegistered;

    private String hospitalNumber;

    private String mobileNumber;

    private String hospitalDepartmentName;

    private String appointmentMode;

    private Double appointmentAmount;

    private String roomNumber;

    private String billingModeName;

    private Long roomId;

    private int totalItems;
}
