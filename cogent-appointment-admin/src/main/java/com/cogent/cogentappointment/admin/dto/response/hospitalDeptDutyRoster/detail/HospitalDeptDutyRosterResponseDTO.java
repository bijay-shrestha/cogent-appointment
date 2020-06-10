package com.cogent.cogentappointment.admin.dto.response.hospitalDeptDutyRoster.detail;

import com.cogent.cogentappointment.admin.dto.response.commons.AuditableResponseDTO;
import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.*;

import java.io.Serializable;
import java.util.Date;

/**
 * @author smriti on 20/05/20
 */
@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class HospitalDeptDutyRosterResponseDTO extends AuditableResponseDTO implements Serializable {

    private Long hddRosterId;

    private Long hospitalId;

    private String hospitalName;

    private Long hospitalDeptId;

    private String hospitalDeptName;

    private Integer rosterGapDuration;

    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "MMM dd, YYYY", timezone = "Asia/Kathmandu")
    private Date fromDate;

    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "MMM dd, YYYY", timezone = "Asia/Kathmandu")
    private Date toDate;

    private Character status;

    private String remarks;

    private Character hasOverrideDutyRoster;

    private Character isRoomEnabled;
}
