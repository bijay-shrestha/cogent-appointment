package com.cogent.cogentappointment.client.query;

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
                    " AND h.room.id = :roomId"+
                    " AND hd.toDate >=:fromDate" +
                    " AND hd.fromDate <=:toDate";

    public static String QUERY_TO_FETCH_HDD_ROSTER_ROOM_DETAIL =
            " SELECT" +
                    " h.id as rosterRoomId," +                                          //[0]
                    " h.room.id as roomId," +                                           //[1]
                    " h.room.roomNumber as roomNumber" +                                //[2]
                    " FROM HospitalDepartmentDutyRosterRoomInfo h" +
                    " WHERE h.status = 'Y'" +
                    " AND h.hospitalDepartmentDutyRoster.id =:id";

    public static String QUERY_TO_FETCH_HDD_ROSTER_ROOM_NUMBER =
            " SELECT" +
                    " h.room.roomNumber as roomNumber" +                                //[0]
                    " FROM HospitalDepartmentDutyRosterRoomInfo h" +
                    " WHERE h.status = 'Y'" +
                    " AND h.hospitalDepartmentDutyRoster.id =:id";

}
