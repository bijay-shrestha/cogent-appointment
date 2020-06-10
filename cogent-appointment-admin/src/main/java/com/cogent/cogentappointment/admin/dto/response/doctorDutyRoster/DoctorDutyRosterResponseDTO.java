package com.cogent.cogentappointment.admin.dto.response.doctorDutyRoster;

import com.cogent.cogentappointment.admin.dto.response.commons.AuditableResponseDTO;
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
public class DoctorDutyRosterResponseDTO extends AuditableResponseDTO implements Serializable {

    private Long id;

    private Long doctorId;

    private String doctorName;

    private String fileUri;

    private Long specializationId;

    private String specializationName;

    private Long hospitalId;

    private String hospitalName;

    private Integer rosterGapDuration;

    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "MMM dd, YYYY", timezone = "Asia/Kathmandu")
    private Date fromDate;

    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "MMM dd, YYYY", timezone = "Asia/Kathmandu")
    private Date toDate;

    private Character status;

    private String remarks;

    private Character hasOverrideDutyRoster;
}
