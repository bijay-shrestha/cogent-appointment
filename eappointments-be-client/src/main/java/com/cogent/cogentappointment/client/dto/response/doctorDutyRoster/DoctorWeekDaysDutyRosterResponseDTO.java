package com.cogent.cogentappointment.client.dto.response.doctorDutyRoster;

import lombok.*;

import java.io.Serializable;
import java.util.Date;

/**
 * @author smriti on 29/11/2019
 */
@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class DoctorWeekDaysDutyRosterResponseDTO implements Serializable {

    private Long doctorWeekDaysDutyRosterId;

    private Date startTime;

    private Date endTime;

    private Character dayOffStatus;

    private Long weekDaysId;

    private String weekDaysName;
}
