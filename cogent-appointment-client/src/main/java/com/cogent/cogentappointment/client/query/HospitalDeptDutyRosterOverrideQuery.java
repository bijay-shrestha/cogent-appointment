package com.cogent.cogentappointment.client.query;

/**
 * @author smriti on 20/05/20
 */
public class HospitalDeptDutyRosterOverrideQuery {

    public static final String QUERY_TO_FETCH_DDR_OVERRIDE_COUNT_WITHOUT_ROOM =
            " SELECT COUNT(dd.id)" +
                    " FROM HospitalDepartmentDutyRosterOverride dd" +
                    " LEFT JOIN HospitalDepartmentDutyRoster dr ON dr.id = dd.hospitalDepartmentDutyRoster.id" +
                    " WHERE dr.status != 'D'" +
                    " AND dd.status = 'Y'" +
                    " AND dr.isRoomEnabled = 'N'" +
                    " AND dd.room.id IS NULL" +
                    " AND dr.hospitalDepartment.id= :hospitalDepartmentId" +
                    " AND dd.toDate >=:fromDate" +
                    " AND dd.fromDate <=:toDate";

    public static final String QUERY_TO_FETCH_DDR_OVERRIDE_COUNT_WITH_ROOM =
            " SELECT COUNT(dd.id)" +
                    " FROM HospitalDepartmentDutyRosterOverride dd" +
                    " LEFT JOIN HospitalDepartmentDutyRoster dr ON dr.id = dd.hospitalDepartmentDutyRoster.id" +
                    " WHERE dr.status != 'D'" +
                    " AND dd.status = 'Y'" +
                    " AND dr.isRoomEnabled = 'Y'" +
                    " AND dd.room.id IS NOT NULL" +
                    " AND dr.hospitalDepartment.id= :hospitalDepartmentId" +
                    " AND dd.room.id =:roomId" +
                    " AND dd.toDate >=:fromDate" +
                    " AND dd.fromDate <=:toDate";
}
