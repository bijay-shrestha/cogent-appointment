package com.cogent.cogentappointment.client.dto.response.appointmentTransfer.AppointmentTransferLog;

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
public class PreviousAppointmentDetails implements Serializable {

    private Date previousDate;

    private String previousTime;

    private String previousDoctor;

    private String previousSpecialization;

    private Double previousAppointmentAmount;
}
