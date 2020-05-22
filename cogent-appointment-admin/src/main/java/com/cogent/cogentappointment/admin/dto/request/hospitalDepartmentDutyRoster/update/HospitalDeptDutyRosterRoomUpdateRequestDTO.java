package com.cogent.cogentappointment.admin.dto.request.hospitalDepartmentDutyRoster.update;

import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;

/**
 * @author smriti on 21/05/20
 */
@Getter
@Setter
public class HospitalDeptDutyRosterRoomUpdateRequestDTO implements Serializable {

    private Long rosterRoomId;

    private Long roomId;

    private Character status;
}
