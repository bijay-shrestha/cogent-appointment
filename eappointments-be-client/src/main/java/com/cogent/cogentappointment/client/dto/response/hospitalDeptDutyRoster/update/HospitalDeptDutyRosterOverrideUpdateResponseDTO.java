package com.cogent.cogentappointment.client.dto.response.hospitalDeptDutyRoster.update;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;

/**
 * @author smriti ON 08/02/2020
 */
@Getter
@Setter
@Builder
public class HospitalDeptDutyRosterOverrideUpdateResponseDTO implements Serializable {

    private Long savedOverrideId;
}
