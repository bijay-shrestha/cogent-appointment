package com.cogent.cogentappointment.client.dto.response.doctorDutyRoster;

import com.fasterxml.jackson.annotation.JsonFormat;
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
public class DoctorDutyRosterTimeResponseDTO implements Serializable {

    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "MMM dd YYYY")
    private Date startTime;

    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "MMM dd YYYY")
    private Date endTime;

    private Character dayOffStatus;

    private Integer rosterGapDuration;
}
