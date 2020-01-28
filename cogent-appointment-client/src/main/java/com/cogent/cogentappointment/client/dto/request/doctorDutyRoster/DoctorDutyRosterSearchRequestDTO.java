package com.cogent.cogentappointment.client.dto.request.doctorDutyRoster;

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
public class DoctorDutyRosterSearchRequestDTO implements Serializable {

    private Long doctorId;

    private Long specializationId;

    private Date fromDate;

    private Date toDate;
}




