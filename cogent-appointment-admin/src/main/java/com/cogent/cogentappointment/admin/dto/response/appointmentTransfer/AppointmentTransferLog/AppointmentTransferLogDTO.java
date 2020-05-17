package com.cogent.cogentappointment.admin.dto.response.appointmentTransfer.AppointmentTransferLog;

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
public class AppointmentTransferLogDTO implements Serializable {

    private Long appointmentId, appointmentTransferId;

    private String status, apptNumber, patientName, mobileNumber, age;

    private Gender gender;

    private String transferredToDate, transferredFromDate;

    private String transferredToTime, transferredFromTime;

    private String transferredToDoctor, transferredFromDoctor;

    private String transferredToSpecialization, transferredFromSpecialization;

    private Double transferredToAmount, transferredFromAmount;

    private Character patientType;

    private Character isFollowUp;

    private String transactionNumber;
}
