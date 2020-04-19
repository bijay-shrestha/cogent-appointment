package com.cogent.cogentappointment.client.dto.response.doctorDutyRoster;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.*;

import java.io.Serializable;
import java.util.Date;

@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class DoctorDutyRosterOverrideResponseDTO implements Serializable {

    private Long doctorDutyRosterOverrideId;

    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "MMM dd YYYY")
    private Date fromDate;

    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "MMM dd YYYY")
    private Date toDate;

    private Date startTime;

    private Date endTime;

    private Character dayOffStatus;

    private String remarks;
}
