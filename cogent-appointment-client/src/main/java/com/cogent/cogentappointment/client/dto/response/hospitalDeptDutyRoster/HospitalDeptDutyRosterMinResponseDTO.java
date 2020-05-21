package com.cogent.cogentappointment.client.dto.response.hospitalDeptDutyRoster;

import com.fasterxml.jackson.annotation.JsonFormat;
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
public class HospitalDeptDutyRosterMinResponseDTO implements Serializable {

    private Long hddRosterId;

    private String hospitalDeptName;

    private Integer rosterGapDuration;

    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "MMM dd, YYYY", timezone = "Asia/Kathmandu")
    private Date fromDate;

    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "MMM dd, YYYY", timezone = "Asia/Kathmandu")
    private Date toDate;

    private Character status;

    private int totalItems;
}
