package com.cogent.cogentappointment.client.dto.response.appointment.approval;


import com.cogent.cogentappointment.persistence.enums.Gender;
import com.fasterxml.jackson.annotation.JsonFormat;
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
public class AppointmentPendingApprovalDetailResponseDTO implements Serializable {

    private Long appointmentId;

    private String hospitalName;

    private String hospitalNumber;


    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "MMM dd,YYYY",timezone = "Asia/Kathmandu")
    private Date appointmentDate;

    private String appointmentNumber;

    private String appointmentTime;

    private String esewaId;

    private String registrationNumber;

    private String patientName;

    private Gender patientGender;

    private String patientAge;

    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "MMM dd,YYYY",timezone = "Asia/Kathmandu")
    private Date patientDob;

    private Character isRegistered;

    private String mobileNumber;

    private String doctorName;

    private String fileUri;

    private String specializationName;

    private String transactionNumber;

    private Double appointmentAmount;

    private Character isSelf;

    private String appointmentMode;

    private Long doctorId,specializationId;

    private Character followUp;


}
