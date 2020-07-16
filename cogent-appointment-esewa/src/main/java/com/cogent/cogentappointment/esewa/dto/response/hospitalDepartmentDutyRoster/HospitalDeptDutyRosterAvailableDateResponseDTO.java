package com.cogent.cogentappointment.esewa.dto.response.hospitalDepartmentDutyRoster;

import lombok.*;

import java.io.Serializable;
import java.util.Date;

/**
 * @author smriti on 11/06/20
 */
@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class HospitalDeptDutyRosterAvailableDateResponseDTO implements Serializable {

    private Date fromDate;

    private Date toDate;

    private Character dayOffStatus;
}
