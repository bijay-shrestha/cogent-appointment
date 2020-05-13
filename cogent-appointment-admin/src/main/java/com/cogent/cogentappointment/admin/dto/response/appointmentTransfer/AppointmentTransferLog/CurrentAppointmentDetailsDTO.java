package com.cogent.cogentappointment.admin.dto.response.appointmentTransfer.AppointmentTransferLog;

import lombok.*;

import java.io.Serializable;
import java.util.Date;

/**
 * @author Sauravi Thapa ON 5/12/20
 */
@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class CurrentAppointmentDetailsDTO implements Serializable {

    private Long appointmentId;

    private Date appointmentDate;

    private String appointmentNumber,status,appointmentTime;

    private String doctor,specialization;

    private Double appointmentAmount;
}
