package com.cogent.cogentappointment.esewa.query;

/**
 * @author smriti on 21/05/20
 */
public class HospitalDeptDutyRosterRoomQuery {

    public static String QUERY_TO_FETCH_HDD_ROSTER_ROOM_DETAIL =
            " SELECT" +
                    " h.room.id as roomId," +                                           //[1]
                    " h.room.roomNumber as roomNumber" +                                //[2]
                    " FROM HospitalDepartmentDutyRosterRoomInfo h" +
                    " LEFT JOIN HospitalDepartmentDutyRoster hd ON hd.id = h.hospitalDepartmentDutyRoster.id" +
                    " WHERE h.status = 'Y'" +
                    " AND h.hospitalDepartmentDutyRoster.status = 'Y'" +
                    " AND hd.id=:id";

}
