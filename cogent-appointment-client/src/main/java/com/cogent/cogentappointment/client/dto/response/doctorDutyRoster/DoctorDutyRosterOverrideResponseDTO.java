package com.cogent.cogentappointment.client.dto.response.doctorDutyRoster;

import lombok.*;

import java.io.Serializable;
import java.util.Date;

@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class DoctorDutyRosterOverrideResponseDTO implements Serializable {

    private Long doctorDutyRosterOverrideId;

    private Date fromDate;

    private Date toDate;

    private Date startTime;

    private Date endTime;

    private Character dayOffStatus;

    private String remarks;
}
