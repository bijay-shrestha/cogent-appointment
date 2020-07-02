package com.cogent.cogentappointment.client.dto.response.reschedule;

import com.cogent.cogentappointment.persistence.enums.Gender;
import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.*;

import java.io.Serializable;
import java.util.Date;

import static com.fasterxml.jackson.annotation.JsonFormat.Shape.STRING;

/**
 * @author Rupak
 */
@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class AppointmentRescheduleLogDTO implements Serializable {

    private String esewaId;

    @JsonFormat(shape = STRING, pattern = "MMM dd,YYYY", timezone = "Asia/Kathmandu")
    private Date previousAppointmentDate;

    @JsonFormat(shape = STRING, pattern = "MMM dd,YYYY", timezone = "Asia/Kathmandu")
    private Date rescheduleAppointmentDate;

    private Character isFollowUp;

    private String previousAppointmentTime;

    private String rescheduleAppointmentTime;

    private String appointmentNumber;

    private String registrationNumber;

    private String patientName;

    private String patientAddress;

    private Gender patientGender;

    private String age;

    private String mobileNumber;

    private String doctorName;

    private String fileUri;

    private String specializationName;

    private String transactionNumber;

    private Double appointmentAmount;

    private String remarks;

    private String hospitalDepartmentName;

    private String roomNumber;

    private String billingModeName;

    private Character isRegistered;
}
