package com.cogent.cogentappointment.esewa.dto.request.doctorDutyRoster;

import com.cogent.cogentappointment.esewa.constraintvalidator.Status;
import lombok.*;

import javax.validation.constraints.NotNull;
import java.io.Serializable;
import java.util.Date;

/**
 * @author smriti on 27/11/2019
 */
@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class DoctorWeekDaysDutyRosterUpdateRequestDTO implements Serializable {

    @NotNull
    private Long doctorWeekDaysDutyRosterId;

    @NotNull
    private Date startTime;

    @NotNull
    private Date endTime;

    @NotNull
    @Status
    private Character dayOffStatus;

    @NotNull
    private Long weekDaysId;

    private String weekDaysName;
}
