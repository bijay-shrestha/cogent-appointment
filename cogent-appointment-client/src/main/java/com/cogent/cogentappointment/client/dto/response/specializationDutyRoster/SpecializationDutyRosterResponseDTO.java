package com.cogent.cogentappointment.client.dto.response.specializationDutyRoster;

import com.cogent.cogentappointment.client.dto.response.common.AuditableResponseDTO;
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
public class SpecializationDutyRosterResponseDTO extends AuditableResponseDTO implements Serializable {

    private Long id;

    private Long specializationId;

    private String specializationName;

    private Integer rosterGapDuration;

    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "MMM dd, YYYY", timezone = "Asia/Kathmandu")
    private Date fromDate;

    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "MMM dd, YYYY", timezone = "Asia/Kathmandu")
    private Date toDate;

    private Character status;

    private String remarks;

    private Character hasOverrideDutyRoster;
}
