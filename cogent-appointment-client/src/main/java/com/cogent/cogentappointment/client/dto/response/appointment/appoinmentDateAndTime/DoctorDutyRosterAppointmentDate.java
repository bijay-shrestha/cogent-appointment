package com.cogent.cogentappointment.client.dto.response.appointment.appoinmentDateAndTime;

import lombok.*;

import java.io.Serializable;
import java.util.Date;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class DoctorDutyRosterAppointmentDate implements Serializable {

    private Long id;

    private Character hasOverride;

    private Date fromDate;

    private Date toDate;
}
