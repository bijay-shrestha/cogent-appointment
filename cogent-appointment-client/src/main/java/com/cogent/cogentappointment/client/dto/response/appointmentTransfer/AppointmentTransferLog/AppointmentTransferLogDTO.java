package com.cogent.cogentappointment.client.dto.response.appointmentTransfer.AppointmentTransferLog;

import com.cogent.cogentappointment.persistence.enums.Gender;
import lombok.*;

import java.io.Serializable;
import java.util.Date;

/**
 * @author Sauravi Thapa ON 5/11/20
 */
@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class AppointmentTransferLogDTO implements Serializable{
    private Long appointmentId;

    private String status;

    private String apptNumber;

    private String patientName;

    private Gender gender;

    private String mobileNumber;

    private Date transferredToDate;

    private Date transferredFromDate;

    private String transferredToTime;

    private String transferredFromTime;

    private String transferredToDoctor;

    private String  transferredFromDoctor;

    private String transferredToSpecialization;

    private String transferredFromSpecialization;

    private Double transferredToAppointmentAmount;

    private Double transferredFromAppointmentAmount;
}
