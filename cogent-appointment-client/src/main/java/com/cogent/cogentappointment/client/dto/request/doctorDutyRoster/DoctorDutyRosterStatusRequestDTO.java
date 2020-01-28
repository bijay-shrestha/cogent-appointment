package com.cogent.cogentappointment.client.dto.request.doctorDutyRoster;

import lombok.*;

import java.io.Serializable;
import java.util.Date;
/**
 * @author smriti ON 13/12/2019
 */
@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class DoctorDutyRosterStatusRequestDTO implements Serializable {

    private Date fromDate;

    private Date toDate;

    private Long doctorId;

    private Long specializationId;
}
