package com.cogent.cogentappointment.admin.query;

import java.util.Objects;

/**
 * @author smriti on 21/05/20
 */
public class HospitalDeptDutyRosterRoomQuery {

    public static String QUERY_TO_FETCH_ROOM_COUNT =
            " SELECT COUNT(h.id)" +
                    " FROM HospitalDepartmentDutyRosterRoomInfo h" +
                    " LEFT JOIN HospitalDepartmentDutyRoster hd ON hd.id = h.hospitalDepartmentDutyRoster.id" +
                    " WHERE" +
                    " hd.status != 'D'" +
                    " AND hd.hospitalDepartment.id= :id" +
                    " AND h.hospitalDepartmentRoomInfo.id = :hospitalDepartmentRoomInfoId" +
                    " AND hd.toDate >=:fromDate" +
                    " AND hd.fromDate <=:toDate";

    public static String QUERY_TO_FETCH_ROOM_COUNT_EXCEPT_CURRENT_ID(Long hospitalDepartmentRoomInfoId) {
        String query = " SELECT COUNT(h.id)" +
                " FROM HospitalDepartmentDutyRosterRoomInfo h" +
                " LEFT JOIN HospitalDepartmentDutyRoster hd ON hd.id = h.hospitalDepartmentDutyRoster.id" +
                " WHERE" +
                " hd.status != 'D'" +
                " AND hd.id !=:id" +
                " AND hd.hospitalDepartment.id= :hospitalDepartmentId" +
                " AND hd.toDate >=:fromDate" +
                " AND hd.fromDate <=:toDate";

        if (!Objects.isNull(hospitalDepartmentRoomInfoId))
            query += " AND h.hospitalDepartmentRoomInfo.id = " + hospitalDepartmentRoomInfoId;

        return query;
    }

    public static String QUERY_TO_FETCH_HDD_ROSTER_ROOM_DETAIL =
            " SELECT" +
                    " h.id as rosterRoomId," +                                          //[0]
                    " hr.id as hospitalDepartmentRoomInfoId," +                         //[1]
                    " hr.room.roomNumber as roomNumber" +                               //[2]
                    " FROM HospitalDepartmentDutyRosterRoomInfo h" +
                    " LEFT JOIN HospitalDepartmentRoomInfo hr ON hr.id = h.hospitalDepartmentRoomInfo.id" +
                    " WHERE h.status = 'Y'" +
                    " AND h.hospitalDepartmentDutyRoster.id =:id";

    public static String QUERY_TO_FETCH_HDD_ROSTER_ROOM_NUMBER =
            " SELECT" +
                    " hr.room.roomNumber as roomNumber" +                                //[0]
                    " FROM HospitalDepartmentDutyRosterRoomInfo h" +
                    " LEFT JOIN HospitalDepartmentRoomInfo hr ON hr.id = h.hospitalDepartmentRoomInfo.id" +
                    " WHERE h.status = 'Y'" +
                    " AND h.hospitalDepartmentDutyRoster.id =:id";

}
