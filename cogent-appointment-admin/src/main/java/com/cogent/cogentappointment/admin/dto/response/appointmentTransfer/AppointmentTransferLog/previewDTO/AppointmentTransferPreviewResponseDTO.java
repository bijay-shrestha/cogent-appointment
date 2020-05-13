package com.cogent.cogentappointment.admin.dto.response.appointmentTransfer.AppointmentTransferLog.previewDTO;

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
public class AppointmentTransferPreviewResponseDTO implements Serializable{

    private String appointmentNumber, patientName,mobileNumber,age;

    private Gender gender;

    private Date transferredToDate,transferredFromDate;

    private String transferredToTime,transferredFromTime;

    private String transferredToDoctor,transferredFromDoctor;

    private String transferredToSpecialization,transferredFromSpecialization;

    private Double transferredToAmount,transferredFromAmount;
}
