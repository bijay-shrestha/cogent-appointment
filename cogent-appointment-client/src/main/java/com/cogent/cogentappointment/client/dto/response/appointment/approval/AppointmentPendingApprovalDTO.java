package com.cogent.cogentappointment.client.dto.response.appointment.approval;


import com.cogent.cogentappointment.persistence.enums.Gender;
import lombok.*;

import java.io.Serializable;
import java.util.Date;

/**
 * @author Rupak
 */
@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class AppointmentPendingApprovalDTO implements Serializable {

    private Long appointmentId;

    private Date appointmentDate;

    private String appointmentNumber;

    private String appointmentTime;

    private String esewaId;

    private String registrationNumber;

    private String patientName;

    private Gender patientGender;

    private String patientAge;

    private Date patientDob;

    private Character isRegistered;

    private String mobileNumber;

    private String doctorName;

    private String specializationName;

    private String transactionNumber;

    private Double appointmentAmount;

    private Double refundAmount;
}
