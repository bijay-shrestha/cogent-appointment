package com.cogent.cogentappointment.client.dto.response.reschedule;

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

    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "MMM dd,YYYY",timezone = "Asia/Kathmandu")
    private Date previousAppointmentDate;

    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "MMM dd,YYYY",timezone = "Asia/Kathmandu")
    private Date rescheduleAppointmentDate;

    private Character isFollowUp;

    private String appointmentTime;

    private String appointmentNumber;

    private String registrationNumber;

    private String patientName;

    private String patientAddress;

    private Gender patientGender;

    private String patientAge;

    private String mobileNumber;

    private String doctorName;

    private String fileUri;

    private String specializationName;

    private String transactionNumber;

    private Double appointmentAmount;

    private String remarks;
}
