package com.cogent.cogentappointment.client.dto.request.specializationDutyRoster;

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
public class SpecializationDutyRosterSearchRequestDTO implements Serializable {

    private Long specializationId;

    private Date fromDate;

    private Date toDate;

    private Character status;
}




