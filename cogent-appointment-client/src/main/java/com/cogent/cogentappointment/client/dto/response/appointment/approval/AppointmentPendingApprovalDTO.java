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
public class AppointmentPendingApprovalDTO implements Serializable {

    private Long appointmentId;

    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "MMM dd,YYYY",timezone = "Asia/Kathmandu")
    private Date appointmentDate;

    private String appointmentNumber;

    private String appointmentTime;

    private String registrationNumber;

    private Long patientId;

    private String patientName,age,address;

    private Gender gender;

    private Character isRegistered;

    private String hospitalNumber;

    private String mobileNumber;

    private String doctorName;

    private String doctorSalutation;

    private String fileUri;

    private String specializationName;

    private String appointmentMode;

    private Double appointmentAmount;

    private Character followUp;

}
