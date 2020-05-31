package com.cogent.cogentappointment.esewa.query;

/**
 * @author smriti on 31/05/20
 */
public class HospitalDeptDutyRosterWeekDaysQuery {

    public static final String QUERY_TO_FETCH_HOSPITAL_DEPT_WEEK_DAYS_INFO =
            " SELECT" +
                    " dw.startTime as startTime," +                                     //[0]
                    " dw.endTime as endTime," +                                         //[1]
                    " dw.dayOffStatus as dayOffStatus," +                               //[2]
                    " d.rosterGapDuration as rosterGapDuration" +                       //[3]
                    " FROM HospitalDepartmentDutyRoster d" +
                    " LEFT JOIN HospitalDepartmentWeekDaysDutyRoster dw ON dw.hospitalDepartmentDutyRoster.id = d.id" +
                    " LEFT JOIN WeekDays w ON w.id = dw.weekDaysId.id" +
                    " WHERE" +
                    " d.status = 'Y'" +
                    " AND d.id=:id" +
                    " AND w.code = :code";
}
