package com.cogent.cogentappointment.esewa.dto.response.appointment.appoinmentDateAndTime;

import lombok.*;

import java.io.Serializable;

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
