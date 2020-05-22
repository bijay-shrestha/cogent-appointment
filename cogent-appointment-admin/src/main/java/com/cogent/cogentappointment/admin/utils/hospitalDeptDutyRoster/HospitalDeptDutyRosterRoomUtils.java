package com.cogent.cogentappointment.admin.utils.hospitalDeptDutyRoster;

import com.cogent.cogentappointment.persistence.model.HospitalDepartmentDutyRoster;
import com.cogent.cogentappointment.persistence.model.HospitalDepartmentDutyRosterRoomInfo;
import com.cogent.cogentappointment.persistence.model.Room;

import static com.cogent.cogentappointment.admin.constants.StatusConstants.ACTIVE;

/**
 * @author smriti on 20/05/20
 */
public class HospitalDeptDutyRosterRoomUtils {

    public static HospitalDepartmentDutyRosterRoomInfo parseRoomDetails(HospitalDepartmentDutyRoster dutyRoster,
                                                                        Room room) {

        HospitalDepartmentDutyRosterRoomInfo roomInfo = new HospitalDepartmentDutyRosterRoomInfo();
        roomInfo.setHospitalDepartmentDutyRoster(dutyRoster);
        roomInfo.setRoom(room);
        roomInfo.setStatus(ACTIVE);
        return roomInfo;
    }

    public static void updateRoomDetails(Room room,
                                         Character status,
                                         HospitalDepartmentDutyRosterRoomInfo rosterRoomInfo) {

        rosterRoomInfo.setRoom(room);
        rosterRoomInfo.setStatus(status);
    }
}
