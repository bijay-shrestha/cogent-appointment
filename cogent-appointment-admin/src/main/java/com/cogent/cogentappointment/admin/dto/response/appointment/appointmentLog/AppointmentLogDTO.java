package com.cogent.cogentappointment.admin.dto.response.appointment.appointmentLog;

import com.cogent.cogentappointment.persistence.enums.Gender;
import lombok.*;

import java.io.Serializable;
import java.util.Date;

/**
 * @author Rupak
 */
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class AppointmentLogDTO implements Serializable {

    private String status;
    
    private String hospitalName;

    private Date appointmentDate;

    private String appointmentNumber;

    private String appointmentTime;

    private String esewaId;

    private String registrationNumber;

    private String patientName;

    private Gender patientGender;

    private String patientAge;

    private Date patientDob;

    private Character isSelf;

    private Character isRegistered;

    private String mobileNumber;

    private String doctorName;

    private String specializationName;

    private String transactionNumber;

    private Double appointmentAmount;

    private Double refundAmount;
}
