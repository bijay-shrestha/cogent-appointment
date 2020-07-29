package com.cogent.cogentappointment.esewa.dto.response.hospitalDepartmentDutyRoster;

import lombok.*;

import java.io.Serializable;
import java.util.Date;

/**
 * @author smriti on 29/05/20
 */
@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class HospitalDeptDutyRosterTimeResponseTO implements Serializable {

    private Date startTime;

    private Date endTime;

    private Character dayOffStatus;

    private Integer rosterGapDuration;
}
