package com.cogent.cogentappointment.client.dto.request.doctorDutyRoster;

import lombok.*;

import java.io.Serializable;
import java.util.Date;

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
