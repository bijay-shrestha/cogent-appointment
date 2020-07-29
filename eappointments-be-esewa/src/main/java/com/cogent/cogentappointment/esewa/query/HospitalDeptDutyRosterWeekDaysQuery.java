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
                    " LEFT JOIN WeekDays w ON w.id = dw.weekDays.id" +
                    " WHERE" +
                    " d.status = 'Y'" +
                    " AND d.id=:id" +
                    " AND w.code = :code";

    public static final String QUERY_TO_FETCH_HOSPITAL_DEPT_WEEK_DAYS_CODE =
            "SELECT" +
                    " w.code as weekDayCode" +
                    " FROM HospitalDepartmentDutyRoster d" +
                    " LEFT JOIN HospitalDepartmentWeekDaysDutyRoster dw ON dw.hospitalDepartmentDutyRoster.id = d.id" +
                    " LEFT JOIN WeekDays w ON w.id = dw.weekDays.id" +
                    " WHERE d.id=:id" +
                    " AND d.status='Y'" +
                    " AND dw.dayOffStatus='N'";
}
