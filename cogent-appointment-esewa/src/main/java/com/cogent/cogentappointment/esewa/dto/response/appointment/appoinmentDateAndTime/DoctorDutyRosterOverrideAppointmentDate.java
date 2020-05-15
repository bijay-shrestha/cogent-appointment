package com.cogent.cogentappointment.esewa.dto.response.appointment.appoinmentDateAndTime;

import lombok.*;

import java.io.Serializable;
import java.util.Date;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class DoctorDutyRosterOverrideAppointmentDate implements Serializable {

    private Date fromDate;

    private Date toDate;

    private Character dayOff;

    private String startTime;

    private String endTime;
}
