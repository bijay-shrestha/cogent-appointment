package com.cogent.cogentappointment.esewa.query;

public class DoctorDutyRosterOverrideQuery {

    public static final String QUERY_TO_FETCH_DOCTOR_DUTY_ROSTER_OVERRIDE_TIME =
            "SELECT d.startTime as startTime," +                             //[0]
                    " d.endTime as endTime," +                              //[1]
                    " d.dayOffStatus as dayOffStatus," +                    //[2]
                    " dd.rosterGapDuration as rosterGapDuration" +          //[3]
                    " FROM DoctorDutyRosterOverride d" +
                    " LEFT JOIN DoctorDutyRoster dd ON dd.id = d.doctorDutyRosterId.id" +
                    " WHERE" +
                    " d.status = 'Y'" +
                    " AND dd.status = 'Y'" +
                    " AND :date BETWEEN d.fromDate AND d.toDate" +
                    " AND dd.doctorId.id = :doctorId" +
                    " AND dd.specializationId.id = :specializationId";


}
