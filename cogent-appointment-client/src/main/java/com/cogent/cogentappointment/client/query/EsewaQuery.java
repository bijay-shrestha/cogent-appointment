package com.cogent.cogentappointment.client.query;

import com.cogent.cogentappointment.client.dto.request.eSewa.AppointmentDetailRequestDTO;

import java.util.Objects;

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
                    " AND ddro.status='Y'";

    public static final String QUERY_TO_FETCH_WEEKDAYS_DUTY_ROSTER_BY_DUTY_ROSTER_ID =
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

    /*FETCH DOCTOR AVAILABLE STATUS FROM DOCTOR DUTY ROSTER OVERRIDE*/
    public static String QUERY_TO_FETCH_DOCTOR_DUTY_ROSTER_OVERRIDE_STATUS(AppointmentDetailRequestDTO requestDTO) {

        String query = "SELECT" +
                " CASE WHEN" +
                "   COUNT(ddro.id)>0" +
                " THEN" +
                "   'Y'" +
                " ELSE" +
                "   'N'" +
                " END AS status," +                                                 //[0]
                " CASE WHEN" +
                "   COUNT(ddro.id)>0" +
                " THEN" +
                "   CONCAT(d.name, ' is available for the day')" +
                " ELSE" +
                "   CONCAT(d.name, ' is not available for the day')" +
                " END AS message" +                                                //[1]
                " FROM DoctorDutyRoster ddr" +
                " LEFT JOIN DoctorDutyRosterOverride ddro ON ddr.id = ddro.doctorDutyRosterId.id" +
                " LEFT JOIN Doctor d ON d.id = ddr.doctorId.id" +
                " WHERE" +
                " ddr.status = 'Y'" +
                " AND ddro.status = 'Y'" +
                " AND ddro.dayOffStatus = 'N'" +
                " AND ddr.hospitalId.id =:hospitalId" +
                " AND (:date BETWEEN ddro.fromDate AND ddro.toDate)";

        if (!Objects.isNull(requestDTO.getDoctorId()))
            query += " AND d.id =:doctorId";

        if (!Objects.isNull(requestDTO.getSpecializationId()))
            query += " AND ddr.specializationId.id =:specializationId";

        return query;
    }

    /*FETCH DOCTOR AVAILABLE STATUS FROM DOCTOR DUTY ROSTER*/
    public static String QUERY_TO_FETCH_DOCTOR_DUTY_ROSTER_STATUS(AppointmentDetailRequestDTO requestDTO) {
        String query = "SELECT" +
                " CASE WHEN" +
                "   COUNT(dw.id)>0" +
                " THEN" +
                "   'Y'" +
                " ELSE" +
                "   'N'" +
                " END AS status," +                                             //[0]
                " CASE WHEN" +
                "   COUNT(dw.id)>0" +
                " THEN" +
                "   CONCAT(d.name, ' is available for the day')" +
                " ELSE" +
                "   CONCAT(d.name, ' is not available for the day')" +
                " END AS message" +                                             //[1]
                " FROM DoctorDutyRoster ddr" +
                " LEFT JOIN DoctorWeekDaysDutyRoster dw ON dw.doctorDutyRosterId.id = ddr.id" +
                " LEFT JOIN Doctor d ON d.id = ddr.doctorId.id" +
                " LEFT JOIN WeekDays w ON w.id = dw.weekDaysId.id" +
                " WHERE" +
                " ddr.status = 'Y'" +
                " AND dw.dayOffStatus = 'N'" +
                " AND w.code = :code" +
                " AND ddr.hospitalId.id =:hospitalId" +
                " AND :date BETWEEN ddr.fromDate AND ddr.toDate";

        if (!Objects.isNull(requestDTO.getDoctorId()))
            query += " AND d.id =:doctorId";

        if (!Objects.isNull(requestDTO.getSpecializationId()))
            query += " AND ddr.specializationId.id =:specializationId";

        return query;
    }

    /*FETCH AVAILABLE DOCTORS AND THEIR SPECIALIZATION ON THE CHOSEN DATE*/
    public static final String QUERY_TO_FETCH_AVAILABLE_DOCTORS_FROM_DDR_OVERRIDE =
            "SELECT" +
                    " d.id AS doctorId," +                                  //[0]
                    " d.name AS doctorName," +                              //[1]
                    " s.id AS specializationId," +                          //[2]
                    " s.name AS specializationName" +                       //[3]
                    " FROM DoctorDutyRosterOverride ddro" +
                    " LEFT JOIN DoctorDutyRoster ddr ON ddr.id = ddro.doctorDutyRosterId.id" +
                    " LEFT JOIN Doctor d ON d.id = ddr.doctorId.id" +
                    " LEFT JOIN Specialization s ON s.id = ddr.specializationId.id" +
                    " WHERE" +
                    " ddr.status = 'Y'" +
                    " AND ddro.status = 'Y'" +
                    " AND ddr.hospitalId.id =:hospitalId" +
                    " AND ddro.dayOffStatus = 'N'" +
                    " AND :date BETWEEN ddro.fromDate AND ddro.toDate";

    public static String QUERY_TO_FETCH_DOCTOR_AVALIABLE_DATES_WITH_SPECILIZATION =
            "SELECT" +
                    " ddr.id as id," +
                    " ddr.fromDate as fromDate," +
                    " ddr.toDate as toDate," +
                    " ddr.hasOverrideDutyRoster as hasOverride," +
                    " s.id as specializationId," +
                    " s.name as specializationName" +
                    " FROM DoctorDutyRoster ddr" +
                    " LEFT JOIN Specialization s ON s.id=ddr.specializationId.id" +
                    " WHERE ddr.doctorId.id=:doctorId" +
                    " AND ddr.status='Y'";

    public static String QUERY_TO_FETCH_DAY_OFF_ROSTER_OVERRIDE_DATES =
            "SELECT" +
                    " ddro.fromDate as fromDate," +
                    " ddro.toDate as toDate" +
                    " FROM DoctorDutyRosterOverride ddro" +
                    " WHERE ddro.doctorDutyRosterId.id=:doctorDutyRosterId" +
                    " AND ddro.dayOffStatus='Y'" +
                    " AND ddro.status='Y'";
}
