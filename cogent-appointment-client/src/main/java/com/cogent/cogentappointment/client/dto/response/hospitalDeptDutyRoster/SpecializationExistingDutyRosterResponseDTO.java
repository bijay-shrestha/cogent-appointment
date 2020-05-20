package com.cogent.cogentappointment.client.dto.response.hospitalDeptDutyRoster;

import com.fasterxml.jackson.annotation.JsonFormat;
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
public class SpecializationExistingDutyRosterResponseDTO implements Serializable {

    private Long specializationDutyRosterId;

    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "MMM dd, YYYY", timezone = "Asia/Kathmandu")
    private Date fromDate;

    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "MMM dd, YYYY", timezone = "Asia/Kathmandu")
    private Date toDate;

    private Integer rosterGapDuration;
}
