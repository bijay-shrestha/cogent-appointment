package com.cogent.cogentappointment.client.dto.response.appointment.appoinmentDateAndTime;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.*;

import java.io.Serializable;
import java.util.Date;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class DoctorDutyRosterOverrideAppointmentDate implements Serializable {

    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "MMM dd YYYY")
    private Date fromDate;

    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "MMM dd YYYY")
    private Date toDate;

    private Character dayOff;

    private String startTime;

    private String endTime;
}
