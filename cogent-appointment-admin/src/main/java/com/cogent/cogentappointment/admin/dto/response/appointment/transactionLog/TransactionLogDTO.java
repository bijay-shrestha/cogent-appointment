package com.cogent.cogentappointment.admin.dto.response.appointment.transactionLog;

import com.cogent.cogentappointment.persistence.enums.Gender;
import lombok.*;

import java.io.Serializable;
import java.util.Date;

/**
 * @author Sauravi Thapa ON 4/19/20
 */
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class TransactionLogDTO implements Serializable{

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

    private Character isRegistered;

    private String mobileNumber;

    private String doctorName;

    private String specializationName;

    private String transactionNumber;

    private Double appointmentAmount;

    private Double refundAmount;

    private Date transactionDate;

    private String appointmentMode;

}
