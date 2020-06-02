package com.cogent.cogentappointment.esewa.dto.response.hospitalDepartmentDutyRoster;

import lombok.*;

import java.io.Serializable;

/**
 * @author smriti on 31/05/20
 */
@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class HospitalDeptDutyRosterRoomInfoResponseDTO implements Serializable {

    private Long hddRosterId;

    private Long roomId;

    private String roomNumber;
}
