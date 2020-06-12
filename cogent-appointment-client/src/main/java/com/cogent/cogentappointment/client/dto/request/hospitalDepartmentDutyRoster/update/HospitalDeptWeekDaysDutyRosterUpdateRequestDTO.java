package com.cogent.cogentappointment.client.dto.request.hospitalDepartmentDutyRoster.update;

import com.cogent.cogentappointment.client.constraintvalidator.Status;
import lombok.*;

import javax.validation.constraints.NotNull;
import java.io.Serializable;
import java.util.Date;
import java.util.List;

/**
 * @author smriti on 27/11/2019
 */
@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class HospitalDeptWeekDaysDutyRosterUpdateRequestDTO implements Serializable {

    @NotNull
    private Long rosterWeekDaysId;

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

    private List<HospitalDeptWeekDaysDutyRosterDoctorInfoUpdateRequestDTO> weekDaysDoctorInfo;

}
