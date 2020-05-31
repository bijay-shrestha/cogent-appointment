package com.cogent.cogentappointment.esewa.query;

import java.util.Objects;

/**
 * @author smriti on 29/05/20
 */
public class HospitalDeptDutyRosterOverrideQuery {

    public static String QUERY_TO_FETCH_HOSPITAL_DEPT_DUTY_ROSTER_OVERRIDE_TIME(Long roomId) {

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
                " AND d.date =:date" +
                " And dd.hospitalDepartment.id=:hospitalDepartmentId";

        if (!Objects.isNull(roomId))
            query += " AND d.room.id=" + roomId;

        return query;
    }


}
