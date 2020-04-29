package com.cogent.cogentappointment.admin.dto.response.reschedule;

import com.cogent.cogentappointment.persistence.enums.Gender;
import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.*;

import java.util.Date;

/**
 * @author Rupak
 */
@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class AppointmentRescheduleLogDTO {

    private String esewaId;

    private String hospitalName;

    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "MMM dd,YYYY",timezone = "Asia/Kathmandu")
    private Date previousAppointmentDate;

    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "MMM dd,YYYY",timezone = "Asia/Kathmandu")
    private Date rescheduleAppointmentDate;

    private String appointmentTime;

    private Character isFollowUp;

    private String appointmentNumber;

    private String registrationNumber;

    private String patientName;

    private String patientAddress;

    private Gender patientGender;

    private String patientAge;

    private String mobileNumber;

    private String doctorName;

    private String specializationName;

    private String transactionNumber;

    private Double appointmentAmount;

    private String remarks;
}
