package com.cogent.cogentappointment.client.query;

/**
 * @author Sauravi Thapa ON 5/6/20
 */
public class AppointmentTransferQuery {
    public static String QUERY_TO_FETCH_DATES_BY_DOCTOR_ID=
            "SELECT" +
                    " ddr.id as id," +
                    " ddr.fromDate as fromDate," +
                    " ddr.toDate as toDate" +
                    " FROM" +
                    " DoctorDutyRoster ddr" +
                    " WHERE" +
                    " ddr.doctorId.id = :doctorId" +
                    " AND ddr.specializationId.id=:specializationId";

    public static String QUERY_TO_FETCH_DATE_AND_TIME_BY_DOCTOR_ID=
            "SELECT" +
                    " ddr.id as id," +
                    " ddr.fromDate as fromDate," +
                    " ddr.toDate as toDate," +
                    " ddr.rosterGapDuration as gapDuration," +
                    " ddr.hasOverrideDutyRoster as hasOverride" +
                    " FROM" +
                    " DoctorDutyRoster ddr" +
                    " WHERE" +
                    " ddr.doctorId.id = :doctorId"+
                    " AND ddr.specializationId.id=:specializationId";

    public static String QUERY_TO_FETCH_OVERRIDE_DATE_AND_TIME_BY_DOCTOR_ID=
            "SELECT" +
                    " ddro.id as id," +
                    " ddro.fromDate as fromDate," +
                    " ddro.toDate as toDate," +
                    " DATE_FORMAT(ddro.startTime, '%H:%i') as startTime,"+
                    " DATE_FORMAT(ddro.endTime, '%H:%i') as endTime,"+
                    " ddr.rosterGapDuration as gapDuration" +
                    " FROM" +
                    " DoctorDutyRosterOverride ddro " +
                    " LEFT JOIN DoctorDutyRoster ddr ON ddr.id=ddro.doctorDutyRosterId.id " +
                    " WHERE" +
                    " ddr.doctorId.id = :doctorId" +
                    " AND ddr.specializationId.id =:specializationId";

    public static String QUERY_TO_GET_DAY_OFF_WEEKS_BY_DUTY_ROSTER_ID=
            "SELECT" +
                    " wd.code " +
                    " FROM" +
                    " DoctorWeekDaysDutyRoster dwddr" +
                    " LEFT JOIN WeekDays wd ON" +
                    " wd.id = dwddr.weekDaysId.id" +
                    " WHERE" +
                    " dwddr.doctorDutyRosterId.id = :doctorDutyRosterId" +
                    " AND dwddr.dayOffStatus = 'Y'";

    public static String QUERY_TO_GET_WEEKS_BY_DUTY_ROSTER_ID=
            "SELECT" +
                    " DATE_FORMAT(dwddr.startTime, '%H:%i') as startTime,"+
                    " DATE_FORMAT(dwddr.endTime, '%H:%i') as endTime"+
                    " FROM" +
                    " DoctorWeekDaysDutyRoster dwddr" +
                    " LEFT JOIN WeekDays wd ON" +
                    " wd.id = dwddr.weekDaysId.id" +
                    " LEFT JOIN DoctorDutyRoster ddr ON ddr.id=dwddr.doctorDutyRosterId.id" +
                    " WHERE" +
                    " ddr.doctorId.id = :doctorId" +
                    " AND dwddr.dayOffStatus = 'N'" +
                    " AND wd.code=:code";

    public static String QUERY_TO_GET_OVERRIDE_DATES_BY_DOCTOR_ID=
            "select" +
                    " ddro.id as id," +
                    " ddro.fromDate as fromDate," +
                    " ddro.toDate as toDate" +
                    " FROM" +
                    " DoctorDutyRosterOverride ddro" +
                    " LEFT JOIN DoctorDutyRoster ddr ON ddr.id=ddro.doctorDutyRosterId.id " +
                    " LEFT JOIN Doctor d On ddr.doctorId.id=d.id" +
                    " WHERE" +
                    " ddr.doctorId.id = :doctorId" +
                    " AND ddr.specializationId.id=:specializationId"+
                    " AND ddro.dayOffStatus = 'N'";

    public static String QUERY_TO_GET_UNAVAILABLE_TIME=
            "SELECT" +
                    " DATE_FORMAT(a.appointmentTime , '%h:%i %p')" +
                    " FROM" +
                    " Appointment a" +
                    " WHERE" +
                    " a.doctorId.id = :doctorId" +
                    " AND a.specializationId.id = :specializationId" +
                    " AND a.appointmentDate = :date" +
                    " AND (a.status = 'PA'" +
                    " OR a.status = 'A')";

    public static String QUERY_TO_GET_DOCTOR_CHARGE_BY_DOCTOR_ID=
            "SELECT" +
                    " appointmentCharge as actualCharge," +
                    " appointmentFollowUpCharge  as followUpCharge" +
                    " FROM " +
                    " DoctorAppointmentCharge dac" +
                    " WHERE" +
                    " dac.doctorId.id =:doctorId";
}
