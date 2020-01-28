package com.cogent.cogentappointment.admin.dto.response.doctorDutyRoster;

import lombok.*;

import java.io.Serializable;
import java.time.LocalDate;

/**
 * @author smriti ON 14/12/2019
 */
@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class DoctorDutyRosterStatusResponseDTO implements Serializable {

    private LocalDate date;

    private String startTime;

    private String endTime;

    private Character dayOffStatus;

    private Integer rosterGapDuration;

    private Long doctorId;

    private String doctorName;

    private Long specializationId;

    private String specializationName;
}
