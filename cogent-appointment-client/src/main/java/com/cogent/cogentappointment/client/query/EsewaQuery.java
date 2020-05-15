package com.cogent.cogentappointment.client.query;

import com.cogent.cogentappointment.client.dto.request.appointment.esewa.AppointmentDetailRequestDTO;
import com.cogent.cogentappointment.client.dto.request.appointment.esewa.AvailableDoctorRequestDTO;

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

    /*SMRITI*/
    /*FETCH DOCTOR AVAILABLE STATUS FROM DOCTOR DUTY ROSTER OVERRIDE*/
    public static String QUERY_TO_FETCH_DOCTOR_DUTY_ROSTER_OVERRIDE_STATUS(AppointmentDetailRequestDTO requestDTO) {

        String query = "SELECT" +
                " d.name AS doctorName," +                                  //[0]
                " ddro.dayOffStatus AS dayOffStatus" +                       //[1]
                " FROM DoctorDutyRoster ddr" +
                " LEFT JOIN DoctorDutyRosterOverride ddro ON ddr.id = ddro.doctorDutyRosterId.id" +
                " LEFT JOIN Doctor d ON d.id = ddr.doctorId.id" +
                " WHERE" +
                " ddr.status = 'Y'" +
                " AND ddro.status = 'Y'" +
                " AND ddr.hospitalId.id =:hospitalId" +
                " AND (:date BETWEEN ddro.fromDate AND ddro.toDate)";

        if (!Objects.isNull(requestDTO.getDoctorId()))
            query += " AND d.id =:doctorId";

        if (!Objects.isNull(requestDTO.getSpecializationId()))
            query += " AND ddr.specializationId.id =:specializationId";

        return query;
    }

    /*SMRITI*/
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
                "   CONCAT(d.name, ' IS AVAILABLE FOR THE DAY')" +
                " ELSE" +
                "   CONCAT(d.name, ' IS NOT AVAILABLE FOR THE DAY')" +
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

    /*SMRITI*/
    /*FETCH AVAILABLE DOCTORS AND THEIR SPECIALIZATION ON THE CHOSEN DATE*/
    public static String QUERY_TO_FETCH_AVAILABLE_DOCTORS_FROM_DDR_OVERRIDE(AppointmentDetailRequestDTO requestDTO) {

        String query = "SELECT" +
                " DISTINCT(d.id) AS doctorId," +                        //[0]
                " d.name AS doctorName," +                              //[1]
                " s.id AS specializationId," +                          //[2]
                " s.name AS specializationName," +                      //[3]
                " ddro.dayOffStatus AS dayOffStatus" +                  //[4]
                " FROM DoctorDutyRosterOverride ddro" +
                " LEFT JOIN DoctorDutyRoster ddr ON ddr.id = ddro.doctorDutyRosterId.id" +
                " LEFT JOIN Doctor d ON d.id = ddr.doctorId.id" +
                " LEFT JOIN Specialization s ON s.id = ddr.specializationId.id" +
                " WHERE" +
                " ddr.status = 'Y'" +
                " AND ddro.status = 'Y'" +
                " AND d.status = 'Y'" +
                " AND s.status = 'Y'" +
                " AND ddr.hospitalId.id =:hospitalId" +
                " AND :date BETWEEN ddro.fromDate AND ddro.toDate";

        if (!Objects.isNull(requestDTO.getSpecializationId()))
            query += " AND s.id =:specializationId";

        return query;
    }

    /*SMRITI*/
    /*FETCH AVAILABLE DOCTORS AND THEIR SPECIALIZATION ON THE SELECTED DATE RANGE*/
    public static String QUERY_TO_FETCH_AVAILABLE_DOCTORS_FROM_DDR_OVERRIDE(AvailableDoctorRequestDTO requestDTO) {

        String query = "SELECT" +
                " DISTINCT(d.id) AS doctorId," +                        //[0]
                " d.name AS doctorName," +                              //[1]
                " s.id AS specializationId," +                          //[2]
                " s.name AS specializationName," +                      //[3]
                " ddro.day_off_status AS dayOffStatus," +               //[4]
                " d.nmc_number AS nmcNumber," +                         //[5]
                " CASE WHEN" +
                " (da.status IS NULL" +
                " OR da.status = 'N')" +
                " THEN NULL" +
                " ELSE" +
                " da.file_uri" +
                " END as fileUri" +                                     //[6]
                " FROM doctor_duty_roster_override ddro" +
                " LEFT JOIN doctor_duty_roster ddr ON ddr.id = ddro.doctor_duty_roster_id" +
                " LEFT JOIN doctor d ON d.id = ddr.doctor_id" +
                " LEFT JOIN specialization s ON s.id = ddr.specialization_id" +
                " LEFT JOIN doctor_avatar da ON d.id = da.doctor_id" +
                " LEFT JOIN(" +
                " SELECT" +
                " GROUP_CONCAT(qa.name) as qualificationAlias," +
                " dq.doctor_id as doctorId" +
                " FROM" +
                " doctor_qualification dq" +
                " LEFT JOIN qualification q ON q.id = dq.qualification_id" +
                " LEFT JOIN qualification_alias qa ON qa.id = q.qualification_alias" +
                " WHERE" +
                " dq.status = 'Y'" +
                " GROUP BY" +
                " dq.doctor_id" +
                " )tbl1 ON tbl1.doctorId = d.id" +
                " WHERE" +
                " ddr.status = 'Y'" +
                " AND ddro.status = 'Y'" +
                " AND d.status = 'Y'" +
                " AND s.status = 'Y'" +
                " AND ddr.hospital_id =:hospitalId";

        if (!Objects.isNull(requestDTO.getFromDate()) && !Objects.isNull(requestDTO.getToDate()))
            query += " AND ddro.to_date >=:fromDate" +
                    " AND ddro.from_date <=:toDate";

        if (!Objects.isNull(requestDTO.getSpecializationId()))
            query += " AND s.id =:specializationId";

        if (!Objects.isNull(requestDTO.getDoctorId()))
            query += " AND d.id =:doctorId";

        return query;
    }

    /*SMRITI*/
    /*FETCH AVAILABLE DOCTORS AND THEIR SPECIALIZATION ON THE CHOSEN DATE FROM DDR*/
    public static String QUERY_TO_FETCH_AVAILABLE_DOCTORS_FROM_DDR(AppointmentDetailRequestDTO requestDTO) {

        String query = "SELECT" +
                " DISTINCT(d.id) AS doctorId," +                 //[0]
                " d.name AS doctorName," +                       //[1]
                " s.id AS specializationId," +                   //[2]
                " s.name AS specializationName," +               //[3]
                " dw.dayOffStatus AS dayOffStatus" +             //[4]
                " FROM DoctorDutyRoster ddr" +
                " LEFT JOIN DoctorWeekDaysDutyRoster dw ON dw.doctorDutyRosterId.id = ddr.id" +
                " LEFT JOIN Doctor d ON d.id = ddr.doctorId.id" +
                " LEFT JOIN Specialization s ON s.id = ddr.specializationId.id" +
                " LEFT JOIN WeekDays w ON w.id = dw.weekDaysId.id" +
                " WHERE" +
                " ddr.status = 'Y'" +
                " AND d.status = 'Y'" +
                " AND s.status = 'Y'" +
                " AND w.code = :code" +
                " AND ddr.hospitalId.id =:hospitalId" +
                " AND :date BETWEEN ddr.fromDate AND ddr.toDate";

        if (!Objects.isNull(requestDTO.getSpecializationId()))
            query += " AND s.id =:specializationId";

        return query;
    }

    /*SMRITI*/
    /*FETCH AVAILABLE DOCTORS AND THEIR SPECIALIZATION ON THE SELECTED DATE RANGE FROM DDR*/
    public static String QUERY_TO_FETCH_AVAILABLE_DOCTORS_FROM_DDR(AvailableDoctorRequestDTO requestDTO) {

        String query = "SELECT" +
                " DISTINCT(d.id) AS doctorId," +                 //[0]
                " d.name AS doctorName," +                       //[1]
                " s.id AS specializationId," +                   //[2]
                " s.name AS specializationName," +               //[3]
                " d.nmc_number AS nmcNumber," +                  //[4]
                " CASE WHEN" +
                " (da.status IS NULL" +
                " OR da.status = 'N')" +
                " THEN NULL" +
                " ELSE" +
                " da.file_uri" +
                " END as fileUri," +                             //[5]
                " tbl1.qualificationAlias as qualificationAlias," +  //[6]
                " d.salutation as doctorSalutation"+
                " FROM doctor_duty_roster ddr" +
                " LEFT JOIN doctor_week_days_duty_roster dw ON dw.doctor_duty_roster_id = ddr.id" +
                " LEFT JOIN doctor d ON d.id = ddr.doctor_id" +
                " LEFT JOIN specialization s ON s.id = ddr.specialization_id" +
                " LEFT JOIN doctor_avatar da ON d.id = da.doctor_id" +
                " LEFT JOIN(" +
                " SELECT" +
                " GROUP_CONCAT(qa.name) as qualificationAlias," +
                " dq.doctor_id as doctorId" +
                " FROM" +
                " doctor_qualification dq" +
                " LEFT JOIN qualification q ON q.id = dq.qualification_id" +
                " LEFT JOIN qualification_alias qa ON qa.id = q.qualification_alias" +
                " WHERE" +
                " dq.status = 'Y'" +
                " GROUP BY" +
                " dq.doctor_id" +
                " )tbl1 ON tbl1.doctorId = d.id" +
                " WHERE" +
                " ddr.status = 'Y'" +
                " AND d.status = 'Y'" +
                " AND s.status = 'Y'" +
                " AND ddr.hospital_id =:hospitalId";

        if (!Objects.isNull(requestDTO.getFromDate()) && !Objects.isNull(requestDTO.getToDate()))
            query += " AND ddr.to_date >=:fromDate" +
                    " AND ddr.from_date <=:toDate";

        if (!Objects.isNull(requestDTO.getSpecializationId()))
            query += " AND s.id =:specializationId";

        if (!Objects.isNull(requestDTO.getDoctorId()))
            query += " AND d.id =:doctorId";

        return query;
    }

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

    public static String QUERY_TO_FETCH_DOCTOR_AVALIABLE_DATES_WITH_DOCTOR =
            "SELECT" +
                    " ddr.id as id," +
                    " ddr.fromDate as fromDate," +
                    " ddr.toDate as toDate," +
                    " ddr.hasOverrideDutyRoster as hasOverride," +
                    " d.id as doctorId," +
                    " d.name as doctorName," +
                    " d.salutation as doctorSalutation"+
                    " FROM DoctorDutyRoster ddr" +
                    " LEFT JOIN Doctor d ON d.id=ddr.doctorId.id" +
                    " WHERE ddr.specializationId.id=:specializationId" +
                    " AND ddr.status='Y'";

    public static String QUERY_TO_FETCH_DAY_OFF_ROSTER_OVERRIDE_DATES =
            "SELECT" +
                    " ddro.fromDate as fromDate," +
                    " ddro.toDate as toDate" +
                    " FROM DoctorDutyRosterOverride ddro" +
                    " WHERE ddro.doctorDutyRosterId.id=:doctorDutyRosterId" +
                    " AND ddro.dayOffStatus='Y'" +
                    " AND ddro.status='Y'";

    public static final String QUERY_TO_FETCH_DUTY_ROSTER_OVERRIDE_BY_DOCTOR_AND_SPECIALIZATION_ID =
            "SELECT" +
                    " ddro.toDate as toDate," +
                    " ddro.fromDate as fromDate," +
                    " DATE_FORMAT(ddro.startTime ,'%H:%i') as startTime," +
                    " DATE_FORMAT(ddro.endTime ,'%H:%i') as endTime," +
                    " ddro.dayOffStatus as dayOff" +
                    " FROM DoctorDutyRosterOverride ddro" +
                    " LEFT JOIN DoctorDutyRoster ddr ON ddr.id=ddro.doctorDutyRosterId.id" +
                    " WHERE " +
                    " ddr.doctorId.id=:doctorId" +
                    " AND ddr.specializationId.id=:specializationId" +
                    " AND ddro.dayOffStatus='Y'" +
                    " AND ddro.status='Y'";
}
