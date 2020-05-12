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

    private String status,apptNumber, patientName,mobileNumber;

    private Gender gender;

    private Date transferredToDate,transferredFromDate;

    private String transferredToTime,transferredFromTime;

    private String transferredToDoctor,transferredFromDoctor;

    private String transferredToSpecialization,transferredFromSpecialization;

    private Double transferredToAppointmentAmount,transferredFromAppointmentAmount;
}
