package com.cogent.cogentappointment.admin.dto.request.doctorDutyRoster;

import lombok.*;

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
public class DoctorDutyRosterOverrideRequestDTO implements Serializable {

    private Date fromDate;

    private Date toDate;

    private Date startTime;

    private Date endTime;

    private Character dayOffStatus;

    private Character status;

    private String remarks;
}
