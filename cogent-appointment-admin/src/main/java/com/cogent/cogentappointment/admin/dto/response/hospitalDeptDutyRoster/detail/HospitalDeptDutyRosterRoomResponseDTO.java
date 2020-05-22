package com.cogent.cogentappointment.admin.dto.response.hospitalDeptDutyRoster.detail;

import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;

/**
 * @author smriti on 20/05/20
 */
@Getter
@Setter
public class HospitalDeptDutyRosterRoomResponseDTO implements Serializable {

    private Long rosterRoomId;

    private Long roomId;

    private String roomNumber;
}
