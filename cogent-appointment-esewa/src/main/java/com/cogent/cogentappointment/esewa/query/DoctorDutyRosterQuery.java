package com.cogent.cogentappointment.esewa.query;

/**
 * @author smriti on 26/11/2019
 */
public class DoctorDutyRosterQuery {

    public static final String QUERY_TO_FETCH_DOCTOR_DUTY_ROSTER_TIME =
            " SELECT" +
                    " dw.startTime as startTime," +                                     //[0]
                    " dw.endTime as endTime," +                                         //[1]
                    " dw.dayOffStatus as dayOffStatus," +                               //[2]
                    " d.rosterGapDuration as rosterGapDuration" +                       //[3]
                    " FROM DoctorDutyRoster d" +
                    " LEFT JOIN DoctorWeekDaysDutyRoster dw ON dw.doctorDutyRosterId.id = d.id" +
                    " LEFT JOIN WeekDays w ON w.id = dw.weekDaysId.id" +
                    " WHERE" +
                    " d.status = 'Y'" +
                    " AND :date BETWEEN d.fromDate AND d.toDate" +
                    " AND d.doctorId.id = :doctorId" +
                    " AND d.specializationId.id = :specializationId" +
                    " AND w.code = :code";


}
