package com.cogent.cogentappointment.admin.dto.response.doctorDutyRoster;

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
public class DoctorRosterOverrideUpdateResponseDTO implements Serializable {

    private Long savedOverrideId;
}
