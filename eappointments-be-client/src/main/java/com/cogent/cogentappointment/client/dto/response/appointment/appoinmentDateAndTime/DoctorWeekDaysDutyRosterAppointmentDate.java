package com.cogent.cogentappointment.client.dto.response.appointment.appoinmentDateAndTime;

import lombok.*;

import java.io.Serializable;
import java.util.Date;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class DoctorWeekDaysDutyRosterAppointmentDate implements Serializable {

    private String weekDay;

    private Character dayOff;

    private String startTime;

    private String endTime;
}
