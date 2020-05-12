package com.cogent.cogentappointment.client.query;

import com.cogent.cogentappointment.client.dto.request.appointmentTransfer.AppointmentTransferSearchRequestDTO;
import org.springframework.util.ObjectUtils;

/**
 * @author Sauravi Thapa ON 5/6/20
 */
public class AppointmentTransferQuery {
    public static String QUERY_TO_FETCH_DATES_BY_DOCTOR_ID =
            "SELECT" +
                    " ddr.id as id," +
                    " ddr.fromDate as fromDate," +
                    " ddr.toDate as toDate" +
                    " FROM" +
                    " DoctorDutyRoster ddr" +
                    " WHERE" +
                    " ddr.doctorId.id = :doctorId" +
                    " AND ddr.specializationId.id=:specializationId";

    public static String QUERY_TO_FETCH_DATE_AND_TIME_BY_DOCTOR_ID =
            "SELECT" +
                    " ddr.id as id," +
                    " ddr.fromDate as fromDate," +
                    " ddr.toDate as toDate," +
                    " ddr.rosterGapDuration as gapDuration," +
                    " ddr.hasOverrideDutyRoster as hasOverride" +
                    " FROM" +
                    " DoctorDutyRoster ddr" +
                    " WHERE" +
                    " ddr.doctorId.id = :doctorId" +
                    " AND ddr.specializationId.id=:specializationId";

    public static String QUERY_TO_FETCH_OVERRIDE_DATE_AND_TIME_BY_DOCTOR_ID =
            "SELECT" +
                    " ddro.id as id," +
                    " ddro.fromDate as fromDate," +
                    " ddro.toDate as toDate," +
                    " DATE_FORMAT(ddro.startTime, '%H:%i') as startTime," +
                    " DATE_FORMAT(ddro.endTime, '%H:%i') as endTime," +
                    " ddr.rosterGapDuration as gapDuration" +
                    " FROM" +
                    " DoctorDutyRosterOverride ddro " +
                    " LEFT JOIN DoctorDutyRoster ddr ON ddr.id=ddro.doctorDutyRosterId.id " +
                    " WHERE" +
                    " ddr.doctorId.id = :doctorId" +
                    " AND ddr.specializationId.id =:specializationId";

    public static String QUERY_TO_GET_DAY_OFF_WEEKS_BY_DUTY_ROSTER_ID =
            "SELECT" +
                    " wd.code " +
                    " FROM" +
                    " DoctorWeekDaysDutyRoster dwddr" +
                    " LEFT JOIN WeekDays wd ON" +
                    " wd.id = dwddr.weekDaysId.id" +
                    " WHERE" +
                    " dwddr.doctorDutyRosterId.id = :doctorDutyRosterId" +
                    " AND dwddr.dayOffStatus = 'Y'";

    public static String QUERY_TO_GET_WEEKS_BY_DUTY_ROSTER_ID =
            "SELECT" +
                    " DATE_FORMAT(dwddr.startTime, '%H:%i') as startTime," +
                    " DATE_FORMAT(dwddr.endTime, '%H:%i') as endTime" +
                    " FROM" +
                    " DoctorWeekDaysDutyRoster dwddr" +
                    " LEFT JOIN WeekDays wd ON" +
                    " wd.id = dwddr.weekDaysId.id" +
                    " LEFT JOIN DoctorDutyRoster ddr ON ddr.id=dwddr.doctorDutyRosterId.id" +
                    " WHERE" +
                    " ddr.doctorId.id = :doctorId" +
                    " AND dwddr.dayOffStatus = 'N'" +
                    " AND wd.code=:code";

    public static String QUERY_TO_GET_OVERRIDE_DATES_BY_DOCTOR_ID =
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
                    " AND ddr.specializationId.id=:specializationId" +
                    " AND ddro.dayOffStatus = 'N'";

    public static String QUERY_TO_GET_UNAVAILABLE_TIME =
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

    public static String QUERY_TO_GET_DOCTOR_CHARGE_BY_DOCTOR_ID =
            "SELECT" +
                    " appointmentCharge as actualCharge," +
                    " appointmentFollowUpCharge  as followUpCharge" +
                    " FROM " +
                    " DoctorAppointmentCharge dac" +
                    " WHERE" +
                    " dac.doctorId.id =:doctorId";

    public static String SELECT_CLAUSE_FOR_SEARCH =
            "select" +
                    " a.id as appointmentId," +
                    " a.status as status," +
                    " a.appointmentNumber as apptNumber," +
                    " p.name as patientName," +
                    " p.gender as gender," +
                    " p.mobileNumber as mobileNumber," +
                    " a.appointmentDate as transferredToDate," +
                    " apt.previousAppointmentDate as transferredFromDate," +
                    " DATE_FORMAT(a.appointmentTime , '%h:%i %p') as  transferredToTime," +
                    " DATE_FORMAT(apt.previousAppointmentTime, '%h:%i %p') as  transferredFromTime," +
                    " d.name as transferredToDoctor," +
                    " td.name as transferredFromDoctor," +
                    " s.name as transferredToSpecialization," +
                    " ts.name as transferredFromSpecialization," +
                    " atd.appointmentAmount as transferredToAppointmentAmount," +
                    " CASE" +
                    " WHEN attd.previousAppointmentAmount IS NULL" +
                    " THEN dac.appointmentCharge" +
                    " ELSE  attd.previousAppointmentAmount END as transferredFromAppointmentAmount" +
                    " FROM Appointment a" +
                    " LEFT JOIN AppointmentTransfer apt ON apt.appointment.id = a.id" +
                    " LEFT JOIN Patient p ON p.id=a.patientId.id " +
                    " LEFT JOIN Doctor d ON a.doctorId.id=d.id " +
                    " LEFT JOIN Doctor td ON td.id=apt.previousDoctorId" +
                    " LEFT JOIN Specialization s ON s.id=a.specializationId.id" +
                    " LEFT JOIN Specialization ts ON ts.id=apt.previousSpecializationId " +
                    " LEFT JOIN AppointmentTransactionDetail atd ON atd.appointment.id=a.id " +
                    " LEFT JOIN AppointmentTransferTransactionDetail attd ON attd.appointmentTransfer.id=apt.id" +
                    " LEFT JOIN PatientMetaInfo pmi ON pmi.patient.id=p.id" +
                    " LEFT JOIN DoctorAppointmentCharge dac ON dac.doctorId.id=td.id " +
                    " WHERE" +
                    " a.hasTransferred = 'Y'" +
                    " AND apt.lastModifiedDate =(" +
                    " SELECT" +
                    "  MAX(at2.lastModifiedDate)" +
                    " FROM" +
                    "  AppointmentTransfer at2 " +
                    " WHERE a.id=at2.appointment.id )";

    public static String WHERE_CLAUSE_FOR_SEARCH(AppointmentTransferSearchRequestDTO requestDTO) {

        String whereClause =" ";

        if (!ObjectUtils.isEmpty(requestDTO.getAppointmentNumber()))
            whereClause += " AND a.appointmentNumber='" + requestDTO.getAppointmentNumber() + "'";

        if (!ObjectUtils.isEmpty(requestDTO.getPatientMetaInfoId()))
            whereClause += " AND pmi.id=" + requestDTO.getPatientMetaInfoId();

        if (!ObjectUtils.isEmpty(requestDTO.getDoctorId()))
            whereClause += " AND (a.doctorId.id=" + requestDTO.getDoctorId() +
                    " OR apt.previousDoctorId=" + requestDTO.getDoctorId() + ")";

        if (!ObjectUtils.isEmpty(requestDTO.getSpecializationId()))
            whereClause += " AND (a.specializationId.id=" + requestDTO.getSpecializationId() +
                    " OR apt.previousSpecializationId=" + requestDTO.getSpecializationId() + ")";

        whereClause+="ORDER BY a.appointmentDate ASC";

        return whereClause;
    }

    public static String QUERY_TO_GET_CURRENT_TRANSFERRED_DETAIL(AppointmentTransferSearchRequestDTO requestDTO) {
        return SELECT_CLAUSE_FOR_SEARCH +
                WHERE_CLAUSE_FOR_SEARCH(requestDTO);
    }

    public static String QUERY_TO_GET_PREVIOUS_INFOS=
            "SELECT " +
                    " at2.previousAppointmentDate as previousDate," +
                    " DATE_FORMAT(at2.previousAppointmentTime,'%h:%i %p') as previousTime ," +
                    " d.name as previousDoctor," +
                    " s.name as previousSpecialization," +
                    " CASE " +
                    " WHEN attd.previousAppointmentAmount IS NULL THEN dac.appointmentCharge " +
                    " WHEN attd.previousAppointmentAmount>0 THEN attd.previousAppointmentAmount  END AS previousAppointmentAmount" +
                    " FROM AppointmentTransfer at2" +
                    " LEFT JOIN Appointment a ON at2.appointment.id = a.id" +
                    " LEFT JOIN AppointmentTransferTransactionDetail attd ON attd.appointmentTransfer.id = at2.id" +
                    " LEFT JOIN Doctor d ON d.id=at2.previousDoctorId" +
                    " LEFT JOIN Specialization s ON s.id=at2.previousSpecializationId " +
                    " LEFT JOIN DoctorAppointmentCharge dac ON dac.doctorId.id=at2.previousDoctorId " +
                    " WHERE" +
                    " a.id = :appointmentId" +
                    " ORDER BY at2.lastModifiedDate DESC";


}
