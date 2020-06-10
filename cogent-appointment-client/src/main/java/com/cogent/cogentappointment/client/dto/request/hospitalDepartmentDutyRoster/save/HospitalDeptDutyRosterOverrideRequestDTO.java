package com.cogent.cogentappointment.client.dto.request.hospitalDepartmentDutyRoster.save;

import lombok.*;

import java.io.Serializable;
import java.util.Date;

/**
 * @author smriti on 20/05/20
 */
@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class HospitalDeptDutyRosterOverrideRequestDTO implements Serializable {

    private Date fromDate;

    private Date toDate;

    private Date startTime;

    private Date endTime;

    private Character dayOffStatus;

    private Character status;

    private String remarks;
}
