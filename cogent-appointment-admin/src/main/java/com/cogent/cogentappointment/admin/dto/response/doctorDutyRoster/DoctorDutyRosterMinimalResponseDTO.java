package com.cogent.cogentappointment.admin.dto.response.doctorDutyRoster;

import lombok.*;

import java.io.Serializable;
import java.util.Date;
/**
 * @author smriti on 28/11/2019
 */
@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class DoctorDutyRosterMinimalResponseDTO implements Serializable {

    private Long id;

    private String doctorName;

    private String specializationName;

    private Integer rosterGapDuration;

    private Date fromDate;

    private Date toDate;

    private Character status;

    private String remarks;

    private int totalItems;
}
