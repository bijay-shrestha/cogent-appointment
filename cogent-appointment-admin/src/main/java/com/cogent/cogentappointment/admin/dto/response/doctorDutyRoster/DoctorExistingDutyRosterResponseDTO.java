package com.cogent.cogentappointment.admin.dto.response.doctorDutyRoster;

import lombok.*;

import java.io.Serializable;
import java.util.Date;

/**
 * @author smriti ON 01/02/2020
 */
@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class DoctorExistingDutyRosterResponseDTO implements Serializable {

    private Long doctorDutyRosterId;

    private Date fromDate;

    private Date toDate;

    private Integer rosterGapDuration;
}
