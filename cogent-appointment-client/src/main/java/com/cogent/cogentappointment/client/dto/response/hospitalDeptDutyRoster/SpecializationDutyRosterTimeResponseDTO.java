package com.cogent.cogentappointment.client.dto.response.hospitalDeptDutyRoster;

import lombok.*;

import java.io.Serializable;
import java.util.Date;

/**
 * @author smriti ON 10/12/2019
 */
@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class SpecializationDutyRosterTimeResponseDTO implements Serializable {

    private Date startTime;

    private Date endTime;

    private Character dayOffStatus;

    private Integer rosterGapDuration;
}
