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

    public static String QUERY_TO_FETCH_DATES_BY_DOCTOR_ID_AND_SPECIALIZATION_ID =
            "SELECT" +
                    " ddr.id as id," +
                    " ddr.fromDate as fromDate," +
                    " ddr.toDate as toDate" +
                    " FROM" +
                    " DoctorDutyRoster ddr" +
                    " WHERE" +
                    " ddr.doctorId.id = :doctorId" +
                    " AND ddr.specializationId.id=:specializationId" +
                    " AND ddr.status='Y'";

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
                    " AND ddr.specializationId.id =:specializationId" +
                    " AND ddro.dayOffStatus='N'";

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

    public static String QUERY_TO_GET_WEEKS_TIME_BY_DOCTOR_ID =
            "SELECT" +
                    " DATE_FORMAT(dwddr.startTime, '%H:%i') as startTime," +
                    " DATE_FORMAT(dwddr.endTime, '%H:%i') as endTime" +
                    " FROM" +
                    " DoctorWeekDaysDutyRoster dwddr" +
                    " LEFT JOIN WeekDays wd ON" +
                    " wd.id = dwddr.weekDaysId.id" +
                    " LEFT JOIN DoctorDutyRoster ddr ON ddr.id=dwddr.doctorDutyRosterId.id" +
                    " WHERE" +
                    " ddr.id = :doctorDutyRosterId" +
                    " AND dwddr.dayOffStatus = 'N'" +
                    " AND wd.code=:code";

    public static String QUERY_TO_GET_OVERRIDE_DATES_BY_DOCTOR_ID =
            "select" +
                    " ddro.id as id," +
                    " ddro.fromDate as fromDate," +
                    " ddro.toDate as toDate," +
                    " ddro.dayOffStatus as dayOffStatus" +
                    " FROM" +
                    " DoctorDutyRosterOverride ddro" +
                    " LEFT JOIN DoctorDutyRoster ddr ON ddr.id=ddro.doctorDutyRosterId.id " +
                    " LEFT JOIN Doctor d On ddr.doctorId.id=d.id" +
                    " WHERE" +
                    " ddr.doctorId.id = :doctorId" +
                    " AND ddr.specializationId.id=:specializationId" +
                    " AND ddro.status='Y'";

    public static String QUERY_TO_GET_UNAVAILABLE_TIME =
            "SELECT" +
                    " DATE_FORMAT(a.appointmentTime , '%h:%i %p')" +
                    " FROM" +
                    " Appointment a" +
                    " LEFT JOIN AppointmentDoctorInfo adi ON adi.appointment.id=a.id" +
                    " WHERE" +
                    " adi.doctor.id = :doctorId" +
                    " AND a.hospitalId.id = :hospitalId" +
                    " AND adi.specialization.id = :specializationId" +
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
                    "  DATE_FORMAT(apt.currentAppointmentDateAndTime ,'%b %d,%Y')  as transferredToDate, " +
                    "  DATE_FORMAT(apt.previousAppointmentDateAndTime ,'%b %d,%Y')  as transferredFromDate, " +
                    "  DATE_FORMAT(apt.currentAppointmentDateAndTime ,'%h:%i %p')  as transferredToTime, " +
                    "  DATE_FORMAT(apt.previousAppointmentDateAndTime ,'%h:%i %p')  as transferredFromTime, " +
                    " CASE WHEN" +
                    " (apt.currentDoctor.salutation is null)" +
                    " THEN apt.currentDoctor.name" +
                    " ELSE" +
                    " CONCAT_WS(' ',apt.currentDoctor.salutation, apt.currentDoctor.name)" +
                    " END as transferredToDoctor," +
                    " CASE WHEN" +
                    " (apt.previousDoctor.salutation is null)" +
                    " THEN apt.previousDoctor.name" +
                    " ELSE" +
                    " CONCAT_WS(' ',apt.previousDoctor.salutation, apt.previousDoctor.name)" +
                    " END as transferredFromDoctor," +
                    "  apt.currentSpecialization.name as transferredToSpecialization, " +
                    "  apt.previousSpecialization.name as transferredFromSpecialization, " +
                    "  attd.currentAppointmentAmount  AS transferredToAmount, " +
                    "  attd.previousAppointmentAmount  AS transferredFromAmount," +
                    "  atd.transactionNumber as transactionNumber," +
                    "  a.isFollowUp as isFollowUp," +
                    "  hpi.isRegistered as patientType," +
                    " CASE" +
                    " WHEN" +
                    " (pda.status is null OR pda.status = 'N')" +
                    " THEN null" +
                    " WHEN" +
                    " pda.fileUri LIKE 'public%'" +
                    " THEN" +
                    " CONCAT(:cdnUrl,SUBSTRING_INDEX(pda.fileUri, 'public', -1))" +
                    " ELSE" +
                    " pda.fileUri" +
                    " END as transferredFromFileUri," +
                    " CASE" +
                    " WHEN" +
                    " (cda.status is null OR cda.status = 'N')" +
                    " THEN null" +
                    " WHEN" +
                    " cda.fileUri LIKE 'public%'" +
                    " THEN" +
                    " CONCAT(:cdnUrl,SUBSTRING_INDEX(cda.fileUri, 'public', -1))" +
                    " ELSE" +
                    " cda.fileUri" +
                    " END as transferredToFileUri," +
                    QUERY_TO_CALCULATE_PATIENT_AGE +
                    " FROM " +
                    " AppointmentTransfer apt  " +
                    " LEFT JOIN Appointment a ON a.id=apt.appointment.id" +
                    " LEFT JOIN Patient p ON p.id=a.patientId.id" +
                    " LEFT JOIN AppointmentTransactionDetail atd ON atd.appointment.id=a.id" +
                    " LEFT JOIN AppointmentTransferTransactionDetail attd ON attd.appointmentTransfer.id=apt.id" +
                    " LEFT JOIN PatientMetaInfo pmi ON pmi.patient.id=p.id" +
                    " LEFT JOIN HospitalPatientInfo hpi ON hpi.patient.id =p.id AND hpi.hospital.id = a.hospitalId.id" +
                    " LEFT JOIN DoctorAvatar pda ON pda.doctorId.id=apt.previousDoctor.id " +
                    " LEFT JOIN DoctorAvatar cda ON cda.doctorId.id=apt.currentDoctor.id " +
                    " WHERE a.hasTransferred='Y'" +
                    " AND a.hospitalId.id=:hospitalId ";

    public static String WHERE_CLAUSE_FOR_SEARCH(AppointmentTransferSearchRequestDTO requestDTO) {

        String whereClause = " ";

        if (!ObjectUtils.isEmpty(requestDTO.getAppointmentFromDate())
                && !ObjectUtils.isEmpty(requestDTO.getAppointmentToDate()))
            whereClause += " And ((apt.previousAppointmentDateAndTime BETWEEN '" + utilDateToSqlDate(requestDTO.getAppointmentFromDate())
                    + "' AND '" + utilDateToSqlDate(requestDTO.getAppointmentToDate()) + "')";

        if (!ObjectUtils.isEmpty(requestDTO.getAppointmentFromDate())
                && !ObjectUtils.isEmpty(requestDTO.getAppointmentToDate()))
            whereClause += " OR (apt.currentAppointmentDateAndTime BETWEEN '" + utilDateToSqlDate(requestDTO.getAppointmentFromDate())
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

    public static String QUERY_TO_FETCH_APPOINTMENT_TRANSFER_DETAIL_BY_ID =
            "SELECT" +
                    " a.appointmentNumber as appointmentNumber," +
                    " p.name as patientName," +
                    " p.gender as gender," +
                    " p.mobileNumber as mobileNumber," +
                    " DATE_FORMAT(apt.currentAppointmentDateAndTime ,'%b %d,%Y')  as transferredToDate, " +
                    " DATE_FORMAT(apt.previousAppointmentDateAndTime ,'%b %d,%Y')  as transferredFromDate, " +
                    " DATE_FORMAT(apt.currentAppointmentDateAndTime,'%h:%i %p') as  transferredToTime," +
                    " DATE_FORMAT(apt.previousAppointmentDateAndTime,'%h:%i %p') as  transferredFromTime," +
                    " attd.currentAppointmentAmount  as transferredToAmount," +
                    " attd.previousAppointmentAmount  as transferredFromAmount," +
                    " apt.remarks," +
                    " CASE WHEN" +
                    " (apt.currentDoctor.salutation is null)" +
                    " THEN apt.currentDoctor.name" +
                    " ELSE" +
                    " CONCAT_WS(' ',apt.currentDoctor.salutation, apt.currentDoctor.name)" +
                    " END as transferredToDoctor," +
                    " CASE WHEN" +
                    " (apt.previousDoctor.salutation is null)" +
                    " THEN apt.previousDoctor.name" +
                    " ELSE" +
                    " CONCAT_WS(' ',apt.previousDoctor.salutation, apt.previousDoctor.name)" +
                    " END as transferredFromDoctor," +
                    " apt.previousSpecialization.name as transferredFromSpecialization," +
                    " apt.currentSpecialization.name as transferredToSpecialization," +
                    " atd.transactionNumber as transactionNumber," +
                    " a.isFollowUp as isFollowUp," +
                    "  hpi.isRegistered as patientType," +
                    " CASE" +
                    " WHEN" +
                    " (pda.status is null OR pda.status = 'N')" +
                    " THEN null" +
                    " WHEN" +
                    " pda.fileUri LIKE 'public%'" +
                    " THEN" +
                    " CONCAT(:cdnUrl,SUBSTRING_INDEX(pda.fileUri, 'public', -1))" +
                    " ELSE" +
                    " pda.fileUri" +
                    " END as transferredFromFileUri," +
                    " CASE" +
                    " WHEN" +
                    " (cda.status is null OR cda.status = 'N')" +
                    " THEN null" +
                    " WHEN" +
                    " cda.fileUri LIKE 'public%'" +
                    " THEN" +
                    " CONCAT(:cdnUrl,SUBSTRING_INDEX(cda.fileUri, 'public', -1))" +
                    " ELSE" +
                    " cda.fileUri" +
                    " END as transferredToFileUri," +
                    QUERY_TO_CALCULATE_PATIENT_AGE + "," +
                    APPOINTMENT_TRANSFER_AUDITABLE_QUERY() +
                    " FROM" +
                    " AppointmentTransfer apt" +
                    " LEFT JOIN Appointment a ON a.id=apt.appointment.id " +
                    " LEFT JOIN AppointmentTransactionDetail atd ON atd.appointment.id=a.id" +
                    " LEFT JOIN AppointmentTransferTransactionDetail attd ON attd.appointmentTransfer.id=apt.id" +
                    " LEFT JOIN Patient p ON p.id=a.patientId.id" +
                    " LEFT JOIN PatientMetaInfo pmi ON pmi.patient.id=p.id " +
                    " LEFT JOIN HospitalPatientInfo hpi ON hpi.patient.id =p.id AND hpi.hospital.id = a.hospitalId.id" +
                    " LEFT JOIN DoctorAvatar pda ON pda.doctorId.id=apt.previousDoctor.id " +
                    " LEFT JOIN DoctorAvatar cda ON cda.doctorId.id=apt.currentDoctor.id " +
                    " WHERE apt.id=:appointmentTransferId";

    public static String APPOINTMENT_TRANSFER_AUDITABLE_QUERY() {
        return " apt.createdBy as createdBy," +
                " apt.createdDate as createdDate," +
                " apt.lastModifiedBy as lastModifiedBy," +
                " apt.lastModifiedDate as lastModifiedDate";
    }

    public static String QUERY_TO_GET_APPOINTMENT_ID_LIST_OF_TRANSFERRED_APPOINTMENT(
            AppointmentTransferSearchRequestDTO requestDTO) {

        String sql = "SELECT" +
                " a.id" +
                " FROM Appointment a" +
                " LEFT JOIN AppointmentDoctorInfo adi ON adi.appointment.id=a.id" +
                " LEFT JOIN PatientMetaInfo pmi ON pmi.patient.id=a.patientId.id" +
                " WHERE a.hasTransferred='Y'" +
                " AND a.hospitalId.id=:hospitalId";

        if (!ObjectUtils.isEmpty(requestDTO.getAppointmentFromDate())
                && !ObjectUtils.isEmpty(requestDTO.getAppointmentToDate()))
            sql += " AND (a.appointmentDate BETWEEN '" + utilDateToSqlDate(requestDTO.getAppointmentFromDate())
                    + "' AND '" + utilDateToSqlDate(requestDTO.getAppointmentToDate()) + "')";

        if (!ObjectUtils.isEmpty(requestDTO.getAppointmentNumber()))
            sql += " AND a.appointmentNumber='" + requestDTO.getAppointmentNumber() + "'";

        if (!ObjectUtils.isEmpty(requestDTO.getDoctorId()))
            sql += " AND adi.doctor.id=" + requestDTO.getDoctorId();

        if (!ObjectUtils.isEmpty(requestDTO.getSpecializationId()))
            sql += " AND adi.specialization.id=" + requestDTO.getSpecializationId();

        if (!ObjectUtils.isEmpty(requestDTO.getPatientMetaInfoId()))
            sql += " AND pmi.id=" + requestDTO.getPatientMetaInfoId();

        return sql;
    }

    public static String QUERY_TO_GET_LATEST_APPOINTMENT_TRANSFERRED_ID_AND_STATUS_BY_APPOINTMENTID =
            "SELECT" +
                    " apt.id as appointmentTransferredId," +
                    " a.status  as status" +
                    " FROM" +
                    " AppointmentTransfer apt" +
                    " LEFT JOIN Appointment a on apt.appointment.id=a.id" +
                    " WHERE apt.appointment.id = :appointmentId" +
                    " AND apt.lastModifiedDate =" +
                    " (SELECT MAX(apt1.lastModifiedDate) FROM AppointmentTransfer apt1 " +
                    " WHERE apt1.appointment.id = :appointmentId)";

}
