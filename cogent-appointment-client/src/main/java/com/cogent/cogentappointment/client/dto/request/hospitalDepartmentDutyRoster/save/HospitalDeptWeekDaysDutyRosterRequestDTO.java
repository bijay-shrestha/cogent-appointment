package com.cogent.cogentappointment.client.dto.request.hospitalDepartmentDutyRoster.save;

import com.cogent.cogentappointment.client.constraintvalidator.Status;
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
public class HospitalDeptWeekDaysDutyRosterRequestDTO implements Serializable {

    @NotNull
    private Date startTime;

    @NotNull
    private Date endTime;

    @NotNull
    @Status
    private Character dayOffStatus;

    @NotNull
    private Long weekDaysId;
}
