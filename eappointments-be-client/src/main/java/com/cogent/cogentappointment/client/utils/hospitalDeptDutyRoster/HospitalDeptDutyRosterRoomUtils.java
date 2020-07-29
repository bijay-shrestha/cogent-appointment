package com.cogent.cogentappointment.client.utils.hospitalDeptDutyRoster;

import com.cogent.cogentappointment.persistence.model.HospitalDepartmentDutyRoster;
import com.cogent.cogentappointment.persistence.model.HospitalDepartmentDutyRosterRoomInfo;
import com.cogent.cogentappointment.persistence.model.HospitalDepartmentRoomInfo;

import static com.cogent.cogentappointment.client.constants.StatusConstants.ACTIVE;

/**
 * @author smriti on 20/05/20
 */
public class HospitalDeptDutyRosterRoomUtils {

    public static HospitalDepartmentDutyRosterRoomInfo parseHospitalDepartmentDutyRosterRoomDetails(
            HospitalDepartmentDutyRoster dutyRoster,
            HospitalDepartmentRoomInfo hospitalDepartmentRoomInfo) {

        HospitalDepartmentDutyRosterRoomInfo roomInfo = new HospitalDepartmentDutyRosterRoomInfo();
        roomInfo.setHospitalDepartmentDutyRoster(dutyRoster);
        roomInfo.setHospitalDepartmentRoomInfo(hospitalDepartmentRoomInfo);
        roomInfo.setStatus(ACTIVE);
        return roomInfo;
    }

    public static void updateRoomDetails(HospitalDepartmentDutyRosterRoomInfo rosterRoomInfo,
                                         HospitalDepartmentRoomInfo hospitalDepartmentRoomInfo,
                                         Character status) {

        rosterRoomInfo.setHospitalDepartmentRoomInfo(hospitalDepartmentRoomInfo);
        rosterRoomInfo.setStatus(status);
    }
}
