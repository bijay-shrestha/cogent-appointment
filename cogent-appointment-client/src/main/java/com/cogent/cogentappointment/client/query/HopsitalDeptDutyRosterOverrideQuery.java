package com.cogent.cogentappointment.client.query;

/**
 * @author smriti on 20/05/20
 */
public class HopsitalDeptDutyRosterOverrideQuery {

    public static final String QUERY_TO_FETCH_DDR_OVERRIDE_COUNT =
            " SELECT COUNT(dd.id)" +
                    " FROM HospitalDepartmentDutyRosterOverride dd" +
                    " LEFT JOIN HospitalDepartmentDutyRoster dr ON dr.id = dd.hospitalDepartmentDutyRoster.id" +
                    " WHERE dr.status != 'D'" +
                    " AND dd.status = 'Y'" +
                    " AND dr.hospitalDepartment.id= :hospitalDepartmentId" +
                    " AND dd.toDate >=:fromDate" +
                    " AND dd.fromDate <=:toDate";
}
