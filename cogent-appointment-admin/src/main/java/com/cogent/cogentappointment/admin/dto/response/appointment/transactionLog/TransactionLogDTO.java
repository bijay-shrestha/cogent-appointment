package com.cogent.cogentappointment.admin.dto.response.appointment.transactionLog;

import com.cogent.cogentappointment.persistence.enums.Gender;
import com.fasterxml.jackson.annotation.JsonFormat;
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

    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "MMM dd,YYYY",timezone = "Asia/Kathmandu")
    private Date appointmentDate;

    private String appointmentNumber;

    private String appointmentTime;

    private String esewaId;

    private String registrationNumber;

    private String patientName;

    private String fileUri;

    private Gender patientGender;

    private String age;

    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "MMM dd,YYYY",timezone = "Asia/Kathmandu")
    private Date patientDob;

    private Character isRegistered;

    private String mobileNumber;

    private String doctorName;

    private String specializationName;

    private String transactionNumber;

    private Double appointmentAmount;

    private Double refundAmount;

    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "MMM dd,YYYY",timezone = "Asia/Kathmandu")
    private Date transactionDate;

    private String appointmentMode;

    private Character isFollowUp;

    private String patientAddress;

    private String transactionTime;

    private Double revenueAmount;

    /*department info*/
    private String hospitalDepartmentName;

    private String roomNumber;

    private String billingModeName;

}
