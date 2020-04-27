package com.cogent.cogentappointment.client.dto.response.doctorDutyRoster;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.*;

import java.io.Serializable;
import java.util.Date;

/**
 * @author smriti on 29/11/2019
 */
@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class DoctorDutyRosterResponseDTO implements Serializable {

    private Long id;

    private Long doctorId;

    private String doctorName;

    private Long specializationId;

    private String specializationName;

    private Integer rosterGapDuration;

    private Date fromDate;

    private Date toDate;

    private Character status;

    private String remarks;

    private Character hasOverrideDutyRoster;
}
