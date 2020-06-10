package com.cogent.cogentappointment.admin.dto.response.appointment.appointmentPendingApproval;


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

    private String patientName;

    private String hospitalNumber;

    private String mobileNumber;

    private String doctorName;

    private String fileUri;

    private String specializationName;

    private String appointmentMode;

    private Double appointmentAmount;

}
