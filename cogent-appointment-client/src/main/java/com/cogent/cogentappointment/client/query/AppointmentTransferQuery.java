package com.cogent.cogentappointment.client.query;

import com.cogent.cogentappointment.client.dto.request.appointmentTransfer.AppointmentTransferSearchRequestDTO;
import org.springframework.util.ObjectUtils;

import static com.cogent.cogentappointment.client.utils.commons.DateUtils.utilDateToSqlDate;

/**
 * @author Sauravi Thapa ON 5/6/20
 */
public class AppointmentTransferQuery {


    public static String QUERY_TO_CALCULATE_PATIENT_AGE =
            " CASE" +
                    " WHEN" +
                    " (((TIMESTAMPDIFF(YEAR, p.dateOfBirth, CURDATE()))<=0) AND" +
                    " ((TIMESTAMPDIFF(MONTH, p.dateOfBirth, CURDATE()) % 12)<=0))" +
                    " THEN" +
                    " CONCAT((FLOOR(TIMESTAMPDIFF(DAY, p.dateOfBirth, CURDATE()) % 30.4375)), ' days')" +
                    " WHEN" +
                    " ((TIMESTAMPDIFF(YEAR, p.dateOfBirth ,CURDATE()))<=0)" +
                    " THEN" +
                    " CONCAT(((TIMESTAMPDIFF(MONTH, p.dateOfBirth, CURDATE()) % 12)), ' months')" +
                    " ELSE" +
                    " CONCAT(((TIMESTAMPDIFF(YEAR, p.dateOfBirth ,CURDATE()))), ' years')" +
                    " END AS age";

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
            "SELECT " +
                    "  a.id as appointmentId," +
                    "  apt.id as appointmentTransferId," +
                    "  'N/A' as status, " +
                    "  a.appointmentNumber as apptNumber, " +
                    "  p.name as patientName, " +
                    "  p.mobileNumber as mobileNumber, " +
                    "  p.gender  as gender, " +
                    "  apt.currentAppointmentDate  as transferredToDate, " +
                    "  apt.previousAppointmentDate  as transferredFromDate, " +
                    "  DATE_FORMAT(apt.currentAppointmentTime ,'%h:%i %p')  as transferredToTime, " +
                    "  DATE_FORMAT(apt.previousAppointmentTime ,'%h:%i %p')  as transferredFromTime, " +
                    "  d.name as transferredToDoctor, " +
                    "  pd.name as transferredFromDoctor, " +
                    "  s.name as transferredToSpecialization, " +
                    "  ps.name as transferredFromSpecialization, " +
                    "  attd.currentAppointmentAmount  AS transferredToAmount, " +
                    "  attd.previousAppointmentAmount  AS transferredFromAmount, " +
                    QUERY_TO_CALCULATE_PATIENT_AGE +
                    " FROM " +
                    " AppointmentTransfer apt  " +
                    " LEFT JOIN Appointment a ON a.id=apt.appointment.id  " +
                    " LEFT JOIN Patient p ON p.id=a.patientId.id  " +
                    " LEFT JOIN Doctor d ON d.id=apt.currentDoctor.id  " +
                    " LEFT JOIN Doctor pd ON pd.id=apt.previousDoctor.id " +
                    " LEFT JOIN Specialization s ON s.id=apt.currentSpecialization.id " +
                    " LEFT JOIN Specialization ps ON ps.id=apt.previousSpecialization.id " +
                    " LEFT JOIN AppointmentTransactionDetail atd ON atd.appointment.id=a.id  " +
                    " LEFT JOIN AppointmentTransferTransactionDetail attd ON attd.appointmentTransfer.id=apt.id " +
                    " LEFT JOIN PatientMetaInfo pmi ON pmi.patient.id=p.id  " +
                    " WHERE a.hasTransferred='Y' ";

    public static String WHERE_CLAUSE_FOR_SEARCH(AppointmentTransferSearchRequestDTO requestDTO) {

        String whereClause = " ";

        if (!ObjectUtils.isEmpty(requestDTO.getAppointmentFromDate())
                && !ObjectUtils.isEmpty(requestDTO.getAppointmentToDate()))
            whereClause += " And ((apt.previousAppointmentDate BETWEEN '" + utilDateToSqlDate(requestDTO.getAppointmentFromDate())
                    + "' AND '" + utilDateToSqlDate(requestDTO.getAppointmentToDate()) + "')";

        if (!ObjectUtils.isEmpty(requestDTO.getAppointmentFromDate())
                && !ObjectUtils.isEmpty(requestDTO.getAppointmentToDate()))
            whereClause += " OR (apt.currentAppointmentDate BETWEEN '" + utilDateToSqlDate(requestDTO.getAppointmentFromDate())
                    + "' AND '" + utilDateToSqlDate(requestDTO.getAppointmentToDate()) + "'))";

        if (!ObjectUtils.isEmpty(requestDTO.getAppointmentNumber()))
            whereClause += " AND a.appointmentNumber='" + requestDTO.getAppointmentNumber() + "'";

        if (!ObjectUtils.isEmpty(requestDTO.getPatientMetaInfoId()))
            whereClause += " AND pmi.id=" + requestDTO.getPatientMetaInfoId();

        if (!ObjectUtils.isEmpty(requestDTO.getDoctorId()))
            whereClause += " AND (apt.previousDoctor.id=" + requestDTO.getDoctorId() +
                    " OR apt.currentDoctor.id=" + requestDTO.getDoctorId() + ")";

        if (!ObjectUtils.isEmpty(requestDTO.getSpecializationId()))
            whereClause += " AND (apt.previousSpecialization.id=" + requestDTO.getSpecializationId() +
                    " OR apt.currentSpecialization.id=" + requestDTO.getSpecializationId() + ")";

        whereClause += "ORDER BY a.id DESC";

        return whereClause;
    }

    public static String QUERY_TO_GET_CURRENT_TRANSFERRED_DETAIL(AppointmentTransferSearchRequestDTO requestDTO) {
        return SELECT_CLAUSE_FOR_SEARCH +
                WHERE_CLAUSE_FOR_SEARCH(requestDTO);
    }

    public static String QUERY_TO_GET_CURRENT_APPOINTMENT_INFOS(AppointmentTransferSearchRequestDTO requestDTO) {
        return SELECT_CLAUSE_FOR_CURRENT_INFO +
                WHERE_CLAUSE_FOR_CURRENT_INFO(requestDTO);
    }


    public static String SELECT_CLAUSE_FOR_CURRENT_INFO =
            "SELECT" +
                    " a.id as appointmentId," +
                    " a.status as status, " +
                    " a.appointmentNumber as appointmentNumber," +
                    " a.appointmentDate as appointmentDate," +
                    " DATE_FORMAT(a.appointmentTime ,'%h:%i %p')  as appointmentTime," +
                    " d.name as doctor," +
                    " s.name as specialization," +
                    " atd.appointmentAmount as appointmentAmount" +
                    " FROM" +
                    " Appointment a" +
                    " LEFT JOIN AppointmentTransactionDetail atd ON a.id=atd.appointment.id" +
                    " LEFT JOIN Doctor d ON d.id=a.doctorId.id" +
                    " LEFT JOIN Specialization s ON s.id=a.specializationId.id" +
                    " LEFT JOIN Patient p ON p.id=a.patientId.id" +
                    " LEFT JOIN PatientMetaInfo pmi ON pmi.patient.id=p.id" +
                    " WHERE a.hasTransferred='Y'";

    public static String WHERE_CLAUSE_FOR_CURRENT_INFO(AppointmentTransferSearchRequestDTO requestDTO) {

        String whereClause = " ";

        if (!ObjectUtils.isEmpty(requestDTO.getAppointmentFromDate())
                && !ObjectUtils.isEmpty(requestDTO.getAppointmentToDate()))
            whereClause += " AND (a.appointmentDate BETWEEN '" + utilDateToSqlDate(requestDTO.getAppointmentFromDate())
                    + "' AND '" + utilDateToSqlDate(requestDTO.getAppointmentToDate()) + "')";

        if (!ObjectUtils.isEmpty(requestDTO.getAppointmentNumber()))
            whereClause += " AND a.appointmentNumber='" + requestDTO.getAppointmentNumber() + "'";

        if (!ObjectUtils.isEmpty(requestDTO.getPatientMetaInfoId()))
            whereClause += " AND pmi.id=" + requestDTO.getPatientMetaInfoId();

        if (!ObjectUtils.isEmpty(requestDTO.getDoctorId()))
            whereClause += " AND a.doctorId.id=" + requestDTO.getDoctorId();

        if (!ObjectUtils.isEmpty(requestDTO.getSpecializationId()))
            whereClause += " AND a.specializationId.id=" + requestDTO.getSpecializationId();

        whereClause += "ORDER BY a.id DESC";

        return whereClause;
    }

    public static String QUERY_TO_FETCH_APPOINTMENT_TRANSFER_DETAIL_BY_ID =
            "SELECT" +
                    " a.appointmentNumber as appointmentNumber," +
                    " p.name as patientName," +
                    " p.gender as gender," +
                    " p.mobileNumber as mobileNumber," +
                    " apt.currentAppointmentDate as transferredToDate," +
                    " apt.previousAppointmentDate as transferredFromDate," +
                    " DATE_FORMAT(apt.currentAppointmentTime,'%h:%i %p') as  transferredToTime," +
                    " DATE_FORMAT(apt.previousAppointmentTime,'%h:%i %p') as  transferredFromTime," +
                    " attd.currentAppointmentAmount  as transferredToAmount," +
                    " attd.previousAppointmentAmount  as transferredFromAmount," +
                    " apt.remarks," +
                    " apt.previousDoctor.name as transferredFromDoctor," +
                    " apt.currentDoctor.name as transferredToDoctor," +
                    " apt.previousSpecialization.name as transferredFromSpecialization," +
                    " apt.currentSpecialization.name as transferredToSpecialization," +
                    QUERY_TO_CALCULATE_PATIENT_AGE+"," +
                    APPOINTMENT_TRANSFER_AUDITABLE_QUERY()+
                    " FROM" +
                    " AppointmentTransfer apt" +
                    " LEFT JOIN Appointment a ON a.id=apt.appointment.id " +
                    " LEFT JOIN AppointmentTransferTransactionDetail attd ON attd.appointmentTransfer.id=apt.id" +
                    " LEFT JOIN Patient p ON p.id=a.patientId.id" +
                    " LEFT JOIN PatientMetaInfo pmi ON pmi.patient.id=p.id " +
                    " WHERE apt.id=:appointmentTransferId";

    public static String APPOINTMENT_TRANSFER_AUDITABLE_QUERY() {
        return " apt.createdBy as createdBy," +
                " apt.createdDate as createdDate," +
                " apt.lastModifiedBy as lastModifiedBy," +
                " apt.lastModifiedDate as lastModifiedDate";
    }


}
