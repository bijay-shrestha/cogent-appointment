package com.cogent.cogentappointment.admin.dto.request.doctorDutyRoster;

import com.cogent.cogentappointment.admin.constraintvalidator.Status;
import lombok.*;

import javax.validation.constraints.NotNull;
import java.io.Serializable;
import java.util.Date;

/**
 * @author smriti on 26/11/2019
 */
@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class DoctorWeekDaysDutyRosterRequestDTO implements Serializable {

    private Date startTime;

    private Date endTime;

    @NotNull
    @Status
    private Character dayOffStatus;

    @NotNull
    private Long weekDaysId;
}
