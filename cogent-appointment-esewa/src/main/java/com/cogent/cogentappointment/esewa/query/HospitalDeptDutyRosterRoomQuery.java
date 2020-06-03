package com.cogent.cogentappointment.esewa.query;

/**
 * @author smriti on 21/05/20
 */
public class HospitalDeptDutyRosterRoomQuery {

    public static String QUERY_TO_FETCH_HDD_ROSTER_ROOM_DETAIL(String hddRosterIds) {
        return " SELECT" +
                " h.id as hospitalDepartmentRoomInfoId," +                           //[0]
                " hr.room.roomNumber as roomNumber," +                               //[1]
                " hd.id as hddRosterId" +                                           //[2]
                " FROM HospitalDepartmentDutyRosterRoomInfo h" +
                " LEFT JOIN HospitalDepartmentDutyRoster hd ON hd.id = h.hospitalDepartmentDutyRoster.id" +
                " LEFT JOIN HospitalDepartmentRoomInfo hr ON hr.id = h.hospitalDepartmentRoomInfo.id" +
                " WHERE h.status = 'Y'" +
                " AND h.hospitalDepartmentDutyRoster.status = 'Y'" +
                " AND hd.id IN (" + hddRosterIds + ")";
    }

    public static String QUERY_TO_FETCH_HDD_ROSTER_ROOM_NUMBER =
            " SELECT" +
                    " hr.room.roomNumber as roomNumber" +
                    " FROM HospitalDepartmentDutyRosterRoomInfo h" +
                    " LEFT JOIN HospitalDepartmentDutyRoster hd ON hd.id = h.hospitalDepartmentDutyRoster.id" +
                    " LEFT JOIN HospitalDepartmentRoomInfo hr ON hr.id = h.hospitalDepartmentRoomInfo.id" +
                    " WHERE h.status = 'Y'" +
                    " AND h.hospitalDepartmentDutyRoster.status = 'Y'" +
                    " AND hd.id =:hddRosterId" +
                    " AND h.id =:hospitalDepartmentRoomInfoId";

}
