package com.cogent.cogentappointment.admin.dto.request.hospitalDepartmentDutyRoster;

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
public class HospitalDeptDutyRosterSearchRequestDTO implements Serializable {

    private Long hospitalDepartmentId;

    private Long hospitalId;

    private Date fromDate;

    private Date toDate;

    private Character status;
}




