package com.cogent.cogentappointment.client.query;

public class EsewaQuery {

    public static final String QUERY_TO_FETCH_DUTY_ROSTER_BY_DOCTOR_AND_SPECIALIZATION_ID =
            "SELECT" +
                    " ddr.id as id," +
                    " ddr.hasOverrideDutyRoster as hasOverride," +
                    " ddr.fromDate as fromDate," +
                    " ddr.toDate as toDate" +
                    " FROM DoctorDutyRoster ddr" +
                    " Where ddr.doctorId.id=:doctorId" +
                    " AND ddr.specializationId.id=:specializationId" +
                    " AND ddr.status='Y'";

    public static final String QUERY_TO_FETCH_DUTY_ROSTER_OVERRIDE_BY_DUTY_ROSTER_ID =
            "SELECT" +
                    " ddro.toDate as toDate," +
                    " ddro.fromDate as fromDate," +
                    " DATE_FORMAT(ddro.startTime ,'%H:%i') as startTime," +
                    " DATE_FORMAT(ddro.endTime ,'%H:%i') as endTime," +
                    " ddro.dayOffStatus as dayOff" +
                    " FROM DoctorDutyRosterOverride ddro" +
                    " WHERE ddro.doctorDutyRosterId.id=:doctorDutyRosterId" +
                    " AND ddro.status='Y'" +
                    " AND ddro.dayOffStatus='Y'";

    public static final String QUERY_TO_FETCH_WEEKDAYS_DUTY_ROSTER_DATA_BY_DUTY_ROSTER_ID =
            "SELECT" +
                    " DATE_FORMAT(dwdr.startTime ,'%H:%i') as startTime," +
                    " DATE_FORMAT(dwdr.endTime ,'%H:%i') as endTime," +
                    " dwdr.dayOffStatus as dayOff," +
                    " wd.code as weekDay" +
                    " FROM DoctorDutyRoster ddr" +
                    " LEFT JOIN DoctorWeekDaysDutyRoster dwdr ON dwdr.doctorDutyRosterId.id =ddr.id" +
                    " LEFT JOIN WeekDays wd ON wd.id=dwdr.weekDaysId.id" +
                    " WHERE ddr.id=:doctorDutyRosterId" +
                    " AND ddr.status='Y'" +
                    " AND dwdr.dayOffStatus='N'";

    public static final String QUERY_TO_FETCH_WEEKDAYS_DUTY_ROSTER_BY_DUTY_ROSTER_ID =
            "SELECT" +
                    " wd.code as weekDay" +
                    " FROM DoctorDutyRoster ddr" +
                    " LEFT JOIN DoctorWeekDaysDutyRoster dwdr ON dwdr.doctorDutyRosterId.id =ddr.id" +
                    " LEFT JOIN WeekDays wd ON wd.id=dwdr.weekDaysId.id" +
                    " WHERE ddr.id=:doctorDutyRosterId" +
                    " AND ddr.status='Y'" +
                    " AND dwdr.dayOffStatus='N'";
}
