package com.cogent.cogentappointment.esewa.query;

import java.util.Objects;

/**
 * @author smriti on 29/05/20
 */
public class HospitalDeptDutyRosterOverrideQuery {

    public static String QUERY_TO_FETCH_HOSPITAL_DEPT_DUTY_ROSTER_OVERRIDE_TIME(Long hospitalDepartmentRoomInfoId) {

        String query = "SELECT d.startTime as startTime," +             //[0]
                " d.endTime as endTime," +                              //[1]
                " d.dayOffStatus as dayOffStatus," +                    //[2]
                " dd.rosterGapDuration as rosterGapDuration" +          //[3]
                " FROM HospitalDepartmentDutyRosterOverride d" +
                " LEFT JOIN HospitalDepartmentDutyRoster dd ON dd.id = d.hospitalDepartmentDutyRoster.id" +
                " WHERE" +
                " dd.status = 'Y'" +
                " AND d.status = 'Y'" +
                " AND dd.id=:id" +
                " AND :date BETWEEN d.fromDate AND d.toDate" +
                " And dd.hospitalDepartment.id=:hospitalDepartmentId";

        if (!Objects.isNull(hospitalDepartmentRoomInfoId))
            query += " AND d.hospitalDepartmentRoomInfo.id=" + hospitalDepartmentRoomInfoId;

        return query;
    }

    public static String QUERY_TO_FETCH_HOSPITAL_DEPT_DUTY_ROSTER_OVERRIDE_DATE =
            "SELECT d.fromDate as fromDate," +                            //[0]
                    " d.toDate as toDate," +                              //[1]
                    " d.dayOffStatus as dayOffStatus" +                  //[2]
                    " FROM HospitalDepartmentDutyRosterOverride d" +
                    " LEFT JOIN HospitalDepartmentDutyRoster dd ON dd.id = d.hospitalDepartmentDutyRoster.id" +
                    " WHERE" +
                    " dd.status = 'Y'" +
                    " AND d.status = 'Y'" +
                    " AND dd.id=:hddRosterId";

}
