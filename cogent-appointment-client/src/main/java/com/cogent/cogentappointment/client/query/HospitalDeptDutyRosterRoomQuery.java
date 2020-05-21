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

}
