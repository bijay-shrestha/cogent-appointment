package com.cogent.cogentappointment.client.query;

import com.cogent.cogentappointment.client.dto.request.appointment.approval.AppointmentPendingApprovalSearchDTO;
import com.cogent.cogentappointment.client.dto.request.appointment.esewa.history.AppointmentSearchDTO;
import com.cogent.cogentappointment.client.dto.request.appointment.log.AppointmentLogSearchDTO;
import com.cogent.cogentappointment.client.dto.request.appointment.refund.AppointmentCancelApprovalSearchDTO;
import com.cogent.cogentappointment.client.dto.request.appointmentStatus.AppointmentStatusRequestDTO;
import com.cogent.cogentappointment.client.dto.request.reschedule.AppointmentRescheduleLogSearchDTO;
import org.springframework.util.ObjectUtils;

import java.util.Objects;
import java.util.function.Function;

import static com.cogent.cogentappointment.client.constants.StatusConstants.AppointmentStatusConstants.VACANT;
import static com.cogent.cogentappointment.client.query.PatientQuery.QUERY_TO_CALCULATE_PATIENT_AGE;
import static com.cogent.cogentappointment.client.query.PatientQuery.QUERY_TO_CALCULATE_PATIENT_AGE_NATIVE;
import static com.cogent.cogentappointment.client.utils.commons.DateUtils.utilDateToSqlDate;

/**
 * @author smriti on 2019-10-22
 */
public class AppointmentQuery {

    /*esewa*/
    public static String QUERY_TO_VALIDATE_APPOINTMENT_EXISTS =
            "SELECT COUNT(a.id)" +
                    " FROM  Appointment a" +
                    " WHERE a.appointmentDate =:appointmentDate" +
                    " AND a.doctorId.id =:doctorId" +
                    " AND a.specializationId.id =:specializationId" +
                    " AND DATE_FORMAT(a.appointmentTime,'%H:%i') =:appointmentTime" +
                    " AND a.status='PA'";
    /*esewa*/
    public static String QUERY_TO_FETCH_LATEST_APPOINTMENT_NUMBER =
            "SELECT a.appointment_number" +
                    " FROM appointment a" +
                    " LEFT JOIN hospital h ON h.id =a.hospital_id" +
                    " WHERE" +
                    " str_to_date(a.created_date_nepali,'%Y-%m-%d')" +
                    " BETWEEN :fromDate AND :toDate" +
                    " AND h.id =:hospitalId" +
                    " ORDER BY a.id DESC LIMIT 1";

    /*USED IN DOCTOR DUTY ROSTER*/
    public static String QUERY_TO_FETCH_BOOKED_APPOINTMENT =
            "SELECT DATE_FORMAT(a.appointmentTime, '%H:%i') as appointmentTime" +               //[0]
                    " FROM Appointment a" +
                    " WHERE" +
                    " a.appointmentDate = :date" +
                    " AND a.doctorId.id = :doctorId" +
                    " AND a.specializationId.id = :specializationId" +
                    " AND a.status = 'PA'";

    /*USED IN DOCTOR DUTY ROSTER*/
    public static final String QUERY_TO_FETCH_BOOKED_APPOINTMENT_DATES =
            "SELECT" +
                    " a.appointmentDate as appointmentDate" +
                    " FROM Appointment a" +
                    " WHERE" +
                    " a.status='PA'" +
                    " AND a.appointmentDate BETWEEN :fromDate AND :toDate" +
                    " AND a.doctorId.id = :doctorId" +
                    " AND a.specializationId.id = :specializationId";

    /*USED IN DOCTOR DUTY ROSTER*/
    public static final String QUERY_TO_FETCH_BOOKED_APPOINTMENT_COUNT =
            "SELECT" +
                    " COUNT(a.appointmentDate) as appointmentDate" +
                    " FROM Appointment a" +
                    " WHERE" +
                    " a.status='PA'" +
                    " AND a.appointmentDate BETWEEN :fromDate AND :toDate" +
                    " AND a.doctorId.id = :doctorId" +
                    " AND a.specializationId.id = :specializationId";


    /*%H - hour (e.g., 00,01,02,…12) IN 24 HOUR FORMAT
    * %h - hour (e.g., 00,01,02,…12) IN 12 HOUR FORMAT
     * %i - minutes (e.g., 00,01,02,…12)
     * %p - AM/PM
     * */
    /*esewa*/
    private static final String QUERY_TO_FETCH_MIN_APPOINTMENT =
            " SELECT" +
                    " a.id as appointmentId," +                                             //[0]
                    " a.appointmentDate as appointmentDate," +                              //[1]
                    " DATE_FORMAT(a.appointmentTime,'%h:%i %p') as appointmentTime," +      //[2]
                    " a.appointmentNumber as appointmentNumber," +                          //[3]
                    " p.name as patientName," +                                             //[4]
                    " d.name as doctorName," +                                              //[5]
                    " s.name as specializationName," +                                      //[6]
                    " h.name as hospitalName," +                                             //[7]
                    " atd.appointmentAmount as appointmentAmount," +                          //[8]
                    " d.salutation as doctorSalutation" +
                    " FROM Appointment a" +
                    " LEFT JOIN Patient p ON p.id = a.patientId.id" +
                    " LEFT JOIN Doctor d ON d.id = a.doctorId.id" +
                    " LEFT JOIN Specialization s ON s.id = a.specializationId.id" +
                    " LEFT JOIN Hospital h ON h.id = a.hospitalId.id" +
                    " LEFT JOIN AppointmentTransactionDetail atd ON atd.appointment.id = a.id";

    /*esewa*/
    public static final String QUERY_TO_FETCH_PENDING_APPOINTMENTS =
            QUERY_TO_FETCH_MIN_APPOINTMENT +
                    " WHERE a.status = 'PA'" +
                    " AND a.appointmentDate BETWEEN :fromDate AND :toDate" +
                    " ORDER BY a.appointmentDate DESC";

    /*esewa*/
    public static final String QUERY_TO_FETCH_REFUND_AMOUNT =
            " SELECT" +
                    " (h.refundPercentage * atd.appointmentAmount)/100" +
                    " FROM Appointment a" +
                    " LEFT JOIN AppointmentTransactionDetail atd ON atd.appointment.id = a.id" +
                    " LEFT JOIN Hospital h ON h.id = a.hospitalId.id" +
                    " WHERE a.id =:id";

    /*esewa*/
    public static final String QUERY_TO_FETCH_APPOINTMENT_DETAILS_BY_ID =
            " SELECT" +
                    " a.appointmentDate as appointmentDate," +                              //[0]
                    " DATE_FORMAT(a.appointmentTime,'%h:%i %p') as appointmentTime," +      //[1]
                    " a.appointmentNumber as appointmentNumber," +                          //[2]
                    " d.name as doctorName," +                                              //[3]
                    " s.name as specializationName," +                                      //[4]
                    " h.name as hospitalName," +                                            //[5]
                    " p.name as patientName," +                                             //[6]
                    " p.mobileNumber as mobileNumber," +                                    //[7]
                    " p.dateOfBirth as dateOfBirth," +                                       //[8]
                    " atd.appointmentAmount as appointmentAmount," +                        //[9]
                    " atd.taxAmount as taxAmount," +                                        //[10]
                    " atd.discountAmount as discountAmount," +                             //[11]
                    " atd.serviceChargeAmount as serviceChargeAmount," +                    //[12]
                    " d.salutation as doctorSalutation" +
                    " FROM Appointment a" +
                    " LEFT JOIN Patient p ON p.id = a.patientId.id" +
                    " LEFT JOIN Doctor d ON d.id = a.doctorId.id" +
                    " LEFT JOIN Specialization s ON s.id = a.specializationId.id" +
                    " LEFT JOIN Hospital h ON h.id = a.hospitalId.id" +
                    " LEFT JOIN AppointmentTransactionDetail atd ON atd.appointment.id = a.id" +
                    " WHERE a.id =:id";

    /*esewa*/
    public static final String QUERY_TO_FETCH_APPOINTMENT_HISTORY =
            QUERY_TO_FETCH_MIN_APPOINTMENT +
                    " WHERE a.status = 'A'" +
                    " AND a.appointmentDate BETWEEN :fromDate AND :toDate" +
                    " ORDER BY a.appointmentDate DESC";

    /*esewa*/
    private static String SELECT_CLAUSE_TO_SEARCH_APPOINTMENT =
            " SELECT" +
                    " a.id as appointmentId," +                                             //[0]
                    " h.id as hospitalId," +                                                //[1]
                    " h.name as hospitalName," +                                            //[2]
                    " p.name as patientName," +                                             //[3]
                    " p.mobileNumber as mobileNumber," +                                    //[4]
                    " p.gender as gender," +                                                //[5]
                    QUERY_TO_CALCULATE_PATIENT_AGE + "," +                                  //[6]
                    " a.appointmentDate as appointmentDate," +                              //[7]
                    " DATE_FORMAT(a.appointmentTime,'%h:%i %p') as appointmentTime," +      //[8]
                    " a.appointmentNumber as appointmentNumber," +                          //[9]
                    " atd.appointmentAmount as appointmentAmount," +                        //[10]
                    " d.id as doctorId," +                                                  //[11]
                    " d.name as doctorName," +                                              //[12]
                    " s.id as specializationId," +                                          //[13]
                    " s.name as specializationName," +                                       //[14]
                    " d.salutation as doctorSalutation" +
                    " FROM Appointment a" +
                    " LEFT JOIN Patient p ON p.id = a.patientId.id" +
                    " LEFT JOIN Doctor d ON d.id = a.doctorId.id" +
                    " LEFT JOIN Specialization s ON s.id = a.specializationId.id" +
                    " LEFT JOIN Hospital h ON h.id = a.hospitalId.id" +
                    " LEFT JOIN AppointmentTransactionDetail atd ON atd.appointment.id = a.id";

    public static String QUERY_TO_FETCH_SEARCH_APPOINTMENT_FOR_SELF(AppointmentSearchDTO searchDTO) {

        String query = SELECT_CLAUSE_TO_SEARCH_APPOINTMENT +
                " WHERE a.isSelf = 'Y'" +
                " AND (a.appointmentDate BETWEEN :fromDate AND :toDate)" +
                " AND p.name =:name" +
                " AND p.mobileNumber = :mobileNumber" +
                " AND p.dateOfBirth =: dateOfBirth";

        if (!ObjectUtils.isEmpty(searchDTO.getStatus()))
            query += " AND a.status = '" + searchDTO.getStatus() + "'";

        if (!Objects.isNull(searchDTO.getHospitalId()))
            query += " AND h.id =" + searchDTO.getHospitalId();

        return query + " ORDER BY a.appointmentDate DESC";
    }

    public static String QUERY_TO_FETCH_SEARCH_APPOINTMENT_FOR_OTHERS(AppointmentSearchDTO searchDTO,
                                                                      String childPatientIds) {

        String query = SELECT_CLAUSE_TO_SEARCH_APPOINTMENT +
                " WHERE a.isSelf = 'N'" +
                " AND (a.appointmentDate BETWEEN :fromDate AND :toDate)" +
                " AND p.id IN (" + childPatientIds + ")";

        if (!ObjectUtils.isEmpty(searchDTO.getStatus()))
            query += " AND a.status = '" + searchDTO.getStatus() + "'";

        return query + " ORDER BY a.appointmentDate DESC";
    }

    /*admin*/
    public static Function<AppointmentRescheduleLogSearchDTO, String> QUERY_TO_FETCH_RESCHEDULE_APPOINTMENT_LOGS =
            (appointmentRescheduleLogSearchDTO) ->
                    " SELECT" +
                            " p.eSewaId as esewaId," +                                                               //[0]
                            " arl.previousAppointmentDate as previousAppointmentDate," +                            //[1]
                            " DATE_FORMAT(arl.previousAppointmentDate, '%h:%i %p') as previousAppointmentTime," +   //[2]
                            " arl.rescheduleDate as rescheduleAppointmentDate," +                                   //[3]
                            " DATE_FORMAT(arl.rescheduleDate, '%h:%i %p') as rescheduleAppointmentTime," +          //[4]
                            " a.appointmentNumber as appointmentNumber," +                                          //[5]
                            " hpi.registrationNumber as registrationNumber," +                                     //[6]
                            " p.name as patientName," +                                                            //[7]
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
                            " END AS patientAge," +                                                       //[8]
                            " p.gender as patientGender," +                                               //[9]
                            " p.mobileNumber as mobileNumber," +                                         //[10]
                            " sp.name as specializationName," +                                         //[11]
                            " CASE WHEN" +
                            " (d.salutation is null)" +
                            " THEN d.name" +
                            " ELSE" +
                            " CONCAT_WS(' ',d.salutation, d.name)" +
                            " END as doctorName," +                                                     //[12]
                            " atd.transactionNumber as transactionNumber," +                            //[13]
                            " atd.appointmentAmount as appointmentAmount," +                            //[14]
                            " arl.remarks as remarks," +                                               //[15]
                            " a.isFollowUp as isFollowUp," +                                           //[16]
                            " da.fileUri as fileUri" +                                                 //[17]
                            " FROM AppointmentRescheduleLog arl" +
                            " LEFT JOIN Appointment a ON a.id=arl.appointmentId.id" +
                            " LEFT JOIN AppointmentDoctorInfo ad ON a.id = ad.appointment.id" +
                            " LEFT JOIN Patient p ON p.id=a.patientId" +
                            " LEFT JOIN PatientMetaInfo pmi ON pmi.patient.id=p.id" +
                            " LEFT JOIN HospitalPatientInfo hpi ON hpi.patient.id =p.id AND hpi.hospital.id = a.hospitalId.id" +
                            " LEFT JOIN Hospital h ON h.id=a.hospitalId" +
                            " LEFT JOIN Specialization sp ON sp.id = ad.specialization.id" +
                            " LEFT JOIN AppointmentTransactionDetail atd ON atd.appointment.id=a.id" +
                            " LEFT JOIN Doctor d ON d.id = ad.doctor.id" +
                            " LEFT JOIN DoctorAvatar da ON da.doctorId.id = d.id" +
                            GET_WHERE_CLAUSE_TO_SEARCH_APPOINTMENT_RESCHEDULE_LOG_DETAILS(appointmentRescheduleLogSearchDTO);

    private static String GET_WHERE_CLAUSE_TO_SEARCH_APPOINTMENT_RESCHEDULE_LOG_DETAILS(
            AppointmentRescheduleLogSearchDTO appointmentRescheduleLogSearchDTO) {

        String whereClause = " WHERE " +
                " hpi.status='Y'" +
                " AND arl.status='RES'" +
                " AND sp.status!='D'" +
                " AND d.status!='D'" +
                " AND arl.rescheduleDate BETWEEN :fromDate AND :toDate" +
                " AND h.id =:hospitalId";

        if (!ObjectUtils.isEmpty(appointmentRescheduleLogSearchDTO.getAppointmentNumber()))
            whereClause += " AND a.appointmentNumber LIKE '%" + appointmentRescheduleLogSearchDTO.getAppointmentNumber() + "%'";

        if (!Objects.isNull(appointmentRescheduleLogSearchDTO.getEsewaId()))
            whereClause += " AND p.eSewaId = '" + appointmentRescheduleLogSearchDTO.getEsewaId() + "'";

        if (!Objects.isNull(appointmentRescheduleLogSearchDTO.getAppointmentId()))
            whereClause += " AND a.id = " + appointmentRescheduleLogSearchDTO.getAppointmentId();

        if (!Objects.isNull(appointmentRescheduleLogSearchDTO.getPatientMetaInfoId()))
            whereClause += " AND pmi.id = " + appointmentRescheduleLogSearchDTO.getPatientMetaInfoId();

        if (!Objects.isNull(appointmentRescheduleLogSearchDTO.getSpecializationId()))
            whereClause += " AND sp.id = " + appointmentRescheduleLogSearchDTO.getSpecializationId();

        if (!ObjectUtils.isEmpty(appointmentRescheduleLogSearchDTO.getPatientType()))
            whereClause += " AND hpi.isRegistered = '" + appointmentRescheduleLogSearchDTO.getPatientType() + "'";

        if (!Objects.isNull(appointmentRescheduleLogSearchDTO.getDoctorId()))
            whereClause += " AND d.id = " + appointmentRescheduleLogSearchDTO.getDoctorId();

        whereClause += " ORDER BY arl.rescheduleDate";

        return whereClause;
    }

    public static String QUERY_TO_CALCULATE_TOTAL_RESCHEDULE_AMOUNT(AppointmentRescheduleLogSearchDTO searchDTO) {
        return
                "SELECT" +
                        " COALESCE(SUM(atd.appointmentAmount),0)" +
                        " FROM AppointmentRescheduleLog arl" +
                        " LEFT JOIN Appointment a ON a.id=arl.appointmentId.id" +
                        " LEFT JOIN AppointmentDoctorInfo ad ON a.id = ad.appointment.id" +
                        " LEFT JOIN Patient p ON p.id=a.patientId" +
                        " LEFT JOIN PatientMetaInfo pmi ON pmi.patient.id=p.id" +
                        " LEFT JOIN HospitalPatientInfo hpi ON hpi.patient.id =p.id AND hpi.hospital.id = a.hospitalId.id" +
                        " LEFT JOIN Hospital h ON h.id = a.hospitalId" +
                        " LEFT JOIN Specialization sp ON sp.id = ad.specialization.id" +
                        " LEFT JOIN AppointmentTransactionDetail atd ON atd.appointment.id=a.id" +
                        " LEFT JOIN Doctor d ON d.id = ad.doctor.id" +
                        GET_WHERE_CLAUSE_TO_SEARCH_APPOINTMENT_RESCHEDULE_LOG_DETAILS(searchDTO);
    }

    /*admin*/
    public static String QUERY_TO_FETCH_APPOINTMENTS_CANCEL_APPROVALS(AppointmentCancelApprovalSearchDTO searchDTO) {
        return " SELECT" +
                " a.id as appointmentId," +
                " a.appointmentDate as appointmentDate," +
                " a.appointmentNumber as appointmentNumber," +
                " DATE_FORMAT(a.appointmentTime,'%h:%i %p') as appointmentTime," +
                " p.name as patientName," +
                " p.eSewaId as eSewaId," +
                " p.mobileNumber as mobileNumber," +
                " CASE WHEN" +
                " (hpi.registrationNumber IS NULL)" +
                " THEN 'N/A'" +
                " ELSE" +
                " hpi.registrationNumber" +
                " END as registrationNumber," +
                " CASE WHEN" +
                " (d.salutation is null)" +
                " THEN d.name" +
                " ELSE" +
                " CONCAT_WS(' ',d.salutation, d.name)" +
                " END as doctorName," +
                " s.name as specializationName," +
                " atd.transactionNumber as transactionNumber," +
                " DATE_FORMAT(ard.cancelledDate,'%M %d, %Y') as cancelledDate," +
                " p.gender as gender," +
                " ard.refundAmount as refundAmount," +
                " a.appointmentModeId.name as appointmentMode, " +
                " hpi.isRegistered as isRegistered," +
                QUERY_TO_CALCULATE_PATIENT_AGE + "," +
                " da.fileUri as fileUri" +
                " FROM Appointment a" +
                " LEFT JOIN AppointmentDoctorInfo ad ON a.id = ad.appointment.id" +
                " LEFT JOIN Patient p ON p.id = a.patientId.id" +
                " LEFT JOIN Doctor d ON d.id = ad.doctor.id" +
                " LEFT JOIN DoctorAvatar da ON da.doctorId.id = d.id" +
                " LEFT JOIN Specialization s ON s.id = ad.specialization.id" +
                " LEFT JOIN Hospital h ON h.id = a.hospitalId.id" +
                " LEFT JOIN AppointmentTransactionDetail atd ON atd.appointment.id = a.id" +
                " LEFT JOIN AppointmentRefundDetail ard ON ard.appointmentId.id = a.id" +
                " LEFT JOIN PatientMetaInfo pm ON pm.patient.id = p.id AND pm.status='Y'" +
                " LEFT JOIN HospitalPatientInfo hpi ON hpi.patient.id =p.id AND hpi.hospital.id = a.hospitalId.id" +
                GET_WHERE_CLAUSE_TO_FETCH_REFUND_APPOINTMENTS(searchDTO);
    }

    private static String GET_WHERE_CLAUSE_TO_FETCH_REFUND_APPOINTMENTS(AppointmentCancelApprovalSearchDTO searchDTO) {
        String whereClause = " WHERE ard.status = 'PA'" +
                " AND s.status!='D'" +
                " AND d.status!='D'" +
                " AND h.id =:hospitalId";

        if (!ObjectUtils.isEmpty(searchDTO.getFromDate()) && !ObjectUtils.isEmpty(searchDTO.getToDate()))
            whereClause += " AND (a.appointmentDate BETWEEN '"
                    + utilDateToSqlDate(searchDTO.getFromDate()) + "' AND '"
                    + utilDateToSqlDate(searchDTO.getToDate()) + "' )";

        if (!ObjectUtils.isEmpty(searchDTO.getAppointmentNumber()))
            whereClause += " AND a.appointmentNumber LIKE '%" + searchDTO.getAppointmentNumber() + "%'";

        if (!Objects.isNull(searchDTO.getPatientMetaInfoId()))
            whereClause += " AND pm.id =" + searchDTO.getPatientMetaInfoId();

        if (!Objects.isNull(searchDTO.getDoctorId()))
            whereClause += " AND d.id=" + searchDTO.getDoctorId();

        if (!Objects.isNull(searchDTO.getSpecializationId()))
            whereClause += " AND s.id=" + searchDTO.getSpecializationId();

        if (!ObjectUtils.isEmpty(searchDTO.getPatientType()))
            whereClause += " AND hpi.isRegistered='" + searchDTO.getPatientType() + "'";

        return whereClause + " ORDER BY a.appointmentDate DESC";
    }

    /*admin*/
    public static String QUERY_TO_FETCH_TOTAL_REFUND_AMOUNT(AppointmentCancelApprovalSearchDTO searchDTO) {
        return " SELECT SUM(ard.refundAmount)" +
                " FROM Appointment a" +
                " LEFT JOIN AppointmentDoctorInfo ad ON a.id = ad.appointment.id" +
                " LEFT JOIN Patient p ON p.id = a.patientId.id" +
                " LEFT JOIN Doctor d ON d.id = ad.doctor.id" +
                " LEFT JOIN Specialization s ON s.id = ad.specialization.id" +
                " LEFT JOIN Hospital h ON h.id = a.hospitalId.id" +
                " LEFT JOIN AppointmentTransactionDetail atd ON atd.appointment.id = a.id" +
                " LEFT JOIN AppointmentRefundDetail ard ON ard.appointmentId.id = a.id" +
                " LEFT JOIN PatientMetaInfo pm ON pm.patient.id = p.id AND pm.status='Y'" +
                " LEFT JOIN HospitalPatientInfo hpi ON hpi.patient.id =p.id AND hpi.hospital.id = a.hospitalId.id" +
                GET_WHERE_CLAUSE_TO_FETCH_REFUND_APPOINTMENTS(searchDTO);
    }

    /*admin*/
    public static Function<AppointmentPendingApprovalSearchDTO, String> QUERY_TO_FETCH_PENDING_APPROVALS =
            (searchRequestDTO) ->
                    "SELECT" +
                            " a.id as appointmentId," +                                                    //[0]
                            " a.appointmentDate as appointmentDate," +                                   //[1]
                            " a.appointmentNumber as appointmentNumber," +                               //[2]
                            " DATE_FORMAT(a.appointmentTime, '%h:%i %p') as appointmentTime," +          //[3]
                            " CASE WHEN" +
                            " (hpi.registrationNumber IS NULL)" +
                            " THEN 'N/A'" +
                            " ELSE" +
                            " hpi.registrationNumber" +
                            " END as registrationNumber," +                                               //[4]
                            " p.name as patientName," +                                                  //[5]
                            " p.mobileNumber as mobileNumber," +                                        //[6]
                            " sp.name as specializationName," +                                         //[7]
                            " d.name as doctorName," +
                            " a.appointmentModeId.name as appointmentMode," +
                            " atd.appointmentAmount as appointmentAmount," +
                            " da.fileUri as fileUri," +
                            " da.fileUri as fileUri," +
                            " a.isFollowUp as followUp," +
                            " hpi.hospitalNumber as hospitalNumber," +
                            " p.id as patientId," +
                            " p.gender as gender," +
                            " hpi.address as address," +
                            " hpi.isRegistered as isRegistered," +
                            QUERY_TO_CALCULATE_PATIENT_AGE +
                            " FROM Appointment a" +
                            " LEFT JOIN AppointmentDoctorInfo ad ON a.id = ad.appointment.id" +
                            " LEFT JOIN Patient p ON a.patientId=p.id" +
                            " LEFT JOIN HospitalPatientInfo hpi ON hpi.patient.id =p.id AND hpi.hospital.id = a.hospitalId.id" +
                            " LEFT JOIN Doctor d ON d.id = ad.doctor.id" +
                            " LEFT JOIN DoctorAvatar da ON da.doctorId.id = d.id" +
                            " LEFT JOIN Specialization sp ON sp.id = ad.specialization.id" +
                            " LEFT JOIN Hospital h ON a.hospitalId=h.id" +
                            " LEFT JOIN PatientMetaInfo pi ON pi.patient.id=p.id AND pi.status='Y'" +
                            " LEFT JOIN AppointmentTransactionDetail atd ON a.id = atd.appointment.id" +
                            " LEFT JOIN HospitalAppointmentServiceType has ON has.id = a.hospitalAppointmentServiceType.id"
                            + GET_WHERE_CLAUSE_TO_SEARCH_PENDING_APPOINTMENT_DETAILS(searchRequestDTO);

    private static String GET_WHERE_CLAUSE_TO_SEARCH_PENDING_APPOINTMENT_DETAILS(
            AppointmentPendingApprovalSearchDTO pendingApprovalSearchDTO) {

        String whereClause = " WHERE " +
                " a.status='PA'" +
                " AND h.id =:hospitalId" +
                " AND has.appointmentServiceType.code =:appointmentServiceTypeCode";

        if (!ObjectUtils.isEmpty(pendingApprovalSearchDTO.getFromDate())
                && !ObjectUtils.isEmpty(pendingApprovalSearchDTO.getToDate()))
            whereClause += " AND (a.appointmentDate BETWEEN '"
                    + utilDateToSqlDate(pendingApprovalSearchDTO.getFromDate()) + "' AND '"
                    + utilDateToSqlDate(pendingApprovalSearchDTO.getToDate()) + "')";

        if (!Objects.isNull(pendingApprovalSearchDTO.getAppointmentId()))
            whereClause += " AND a.id = " + pendingApprovalSearchDTO.getAppointmentId();

        if (!Objects.isNull(pendingApprovalSearchDTO.getPatientMetaInfoId()))
            whereClause += " AND pi.id = " + pendingApprovalSearchDTO.getPatientMetaInfoId();

        if (!Objects.isNull(pendingApprovalSearchDTO.getSpecializationId()))
            whereClause += " AND sp.id = " + pendingApprovalSearchDTO.getSpecializationId();

        if (!Objects.isNull(pendingApprovalSearchDTO.getPatientType()))
            whereClause += " AND hpi.isRegistered = '" + pendingApprovalSearchDTO.getPatientType() + "'";

        if (!Objects.isNull(pendingApprovalSearchDTO.getDoctorId()))
            whereClause += " AND d.id = " + pendingApprovalSearchDTO.getDoctorId();

        if (!ObjectUtils.isEmpty(pendingApprovalSearchDTO.getAppointmentNumber()))
            whereClause += " AND a.appointmentNumber LIKE '%" + pendingApprovalSearchDTO.getAppointmentNumber() + "%'";

        whereClause += " ORDER BY a.appointmentDate DESC";

        return whereClause;
    }

    /*admin*/
    public static Function<AppointmentLogSearchDTO, String> QUERY_TO_FETCH_APPOINTMENT_LOGS =
            (appointmentLogSearchDTO) ->
                    "SELECT" +
                            " a.appointmentDate as appointmentDate," +                              //[0]
                            " a.appointmentNumber as appointmentNumber," +                          //[1]
                            " DATE_FORMAT(a.appointmentTime, '%h:%i %p') as appointmentTime," +     //[2]
                            " p.eSewaId as esewaId," +                                              //[3]
                            " hpi.registrationNumber as registrationNumber," +                      //[4]
                            " p.name as patientName," +                                             //[5]
                            " p.gender as patientGender," +                                         //[6]
                            " p.dateOfBirth as patientDob," +                                       //[7]
                            " hpi.isRegistered as isRegistered," +                                  //[8]
                            " p.mobileNumber as mobileNumber," +                                   //[9]
                            " sp.name as specializationName," +                                    //[10]
                            " atd.transactionNumber as transactionNumber," +                       //[11]
                            " atd.appointmentAmount as appointmentAmount," +                       //[12]
                            " CASE WHEN" +
                            " (d.salutation is null)" +
                            " THEN d.name" +
                            " ELSE" +
                            " CONCAT_WS(' ',d.salutation, d.name)" +
                            " END as doctorName," +                                                //[13]
                            " a.status as status," +                                               //[14]
                            " ard.refundAmount as refundAmount," +                                 //[15]
                            " hpi.address as patientAddress," +                                    //[16]
                            " atd.transactionDate as transactionDate," +                           //[17]
                            " am.name as appointmentMode," +                                        //[18]
                            " a.isFollowUp as isFollowUp," +                                        //[19]
                            " (atd.appointmentAmount - COALESCE(ard.refundAmount,0)) as revenueAmount," + //[20]
                            " da.fileUri as fileUri" +                                                  //[21]
                            " FROM Appointment a" +
                            " LEFT JOIN AppointmentDoctorInfo ad ON a.id = ad.appointment.id" +
                            " LEFT JOIN AppointmentMode am ON am.id=a.appointmentModeId.id" +
                            " LEFT JOIN Patient p ON a.patientId.id=p.id" +
                            " LEFT JOIN HospitalPatientInfo hpi ON hpi.patient.id =p.id AND hpi.hospital.id = a.hospitalId.id" +
                            " LEFT JOIN Doctor d ON d.id = ad.doctor.id" +
                            " LEFT JOIN DoctorAvatar da ON da.doctorId.id = d.id" +
                            " LEFT JOIN Specialization sp ON sp.id = ad.specialization.id" +
                            " LEFT JOIN Hospital h ON a.hospitalId.id=h.id" +
                            " LEFT JOIN PatientMetaInfo pi ON pi.patient.id=p.id AND pi.status='Y'" +
                            " LEFT JOIN AppointmentTransactionDetail atd ON a.id = atd.appointment.id" +
                            " LEFT JOIN AppointmentRefundDetail ard ON ard.appointmentId=a.id AND ard.status='A'" +
                            " LEFT JOIN HospitalAppointmentServiceType has ON has.id = a.hospitalAppointmentServiceType.id" +
                            GET_WHERE_CLAUSE_TO_SEARCH_APPOINTMENT_LOG_DETAILS(appointmentLogSearchDTO);

    private static String GET_WHERE_CLAUSE_TO_SEARCH_APPOINTMENT_LOG_DETAILS(
            AppointmentLogSearchDTO appointmentLogSearchDTO) {

        String whereClause = " WHERE" +
                " sp.status!='D'" +
                " AND d.status!='D'" +
                " AND h.id=:hospitalId" +
                " AND has.appointmentServiceType.code =:appointmentServiceTypeCode";

        if (!ObjectUtils.isEmpty(appointmentLogSearchDTO.getFromDate())
                && !ObjectUtils.isEmpty(appointmentLogSearchDTO.getToDate()))
            whereClause += " AND (a.appointmentDate BETWEEN '" + utilDateToSqlDate(appointmentLogSearchDTO.getFromDate())
                    + "' AND '" + utilDateToSqlDate(appointmentLogSearchDTO.getToDate()) + "')";

        if (!Objects.isNull(appointmentLogSearchDTO.getStatus()) && !appointmentLogSearchDTO.getStatus().equals(""))
            whereClause += " AND a.status = '" + appointmentLogSearchDTO.getStatus() + "'";

        if (!Objects.isNull(appointmentLogSearchDTO.getAppointmentId()))
            whereClause += " AND a.id = " + appointmentLogSearchDTO.getAppointmentId();

        if (!Objects.isNull(appointmentLogSearchDTO.getPatientMetaInfoId()))
            whereClause += " AND pi.id = " + appointmentLogSearchDTO.getPatientMetaInfoId();

        if (!Objects.isNull(appointmentLogSearchDTO.getSpecializationId()))
            whereClause += " AND sp.id = " + appointmentLogSearchDTO.getSpecializationId();

        if (!ObjectUtils.isEmpty(appointmentLogSearchDTO.getPatientType()))
            whereClause += " AND hpi.isRegistered = '" + appointmentLogSearchDTO.getPatientType() + "'";

        if (!ObjectUtils.isEmpty(appointmentLogSearchDTO.getAppointmentCategory()))
            whereClause += " AND a.isSelf = '" + appointmentLogSearchDTO.getAppointmentCategory() + "'";

        if (!Objects.isNull(appointmentLogSearchDTO.getDoctorId()))
            whereClause += " AND d.id = " + appointmentLogSearchDTO.getDoctorId();

        whereClause += " ORDER BY a.appointmentDate DESC";

        return whereClause;
    }

    /*admin*/
    public static String QUERY_TO_FETCH_APPOINTMENT_FOR_APPOINTMENT_STATUS(AppointmentStatusRequestDTO requestDTO) {

        String SQL = " SELECT" +
                " a.appointment_date as date," +                                                        //[0]
                " GROUP_CONCAT(DATE_FORMAT(a.appointment_time, '%H:%i'), '-', a.status)" +
                " as startTimeDetails," +                                                               //[1]
                " d.id as doctorId," +                                                                  //[2]
                " s.id as specializationId," +                                                          //[3]
                " a.appointment_number as appointmentNumber," +                                         //[4]
                " p.name as patientName," +                                                             //[5]
                " p.gender as gender," +                                                                //[6]
                " p.mobile_number as mobileNumber," +                                                   //[7]
                QUERY_TO_CALCULATE_PATIENT_AGE_NATIVE + "," +                                           //[8]
                " a.id as appointmentId," +                                                              //[9]
                " a.is_follow_up as isFollowUp," +                                                        //[10]
                " a.has_transferred as hastransferred" +                                                   //[11]
                " FROM appointment a" +
                " INNER JOIN appointment_doctor_info ad ON a.id = ad.appointment_id" +
                " INNER JOIN doctor d ON d.id = ad.doctor_id" +
                " INNER JOIN specialization s ON s.id = ad.specialization_id" +
                " LEFT JOIN hospital h ON h.id = a.hospital_id" +
                " LEFT JOIN patient p ON p.id = a.patient_id" +
                " WHERE" +
                " a.appointment_date BETWEEN :fromDate AND :toDate" +
                " AND a.status IN ('PA', 'A', 'C')" +
                " AND h.id =:hospitalId";

        if (!Objects.isNull(requestDTO.getDoctorId()))
            SQL += " AND d.id =:doctorId";

        if (!Objects.isNull(requestDTO.getSpecializationId()))
            SQL += " AND s.id = :specializationId ";

        if ((!ObjectUtils.isEmpty(requestDTO.getStatus())) && (!(requestDTO.getStatus().equals(VACANT))))
            SQL += " AND a.status='" + requestDTO.getStatus() + "'";

        if (!ObjectUtils.isEmpty(requestDTO.getAppointmentNumber()))
            SQL += " AND a.appointment_number='" + requestDTO.getAppointmentNumber() + "'";

        SQL += " GROUP BY a.appointment_date, a.doctor_id, a.specialization_id, a.id" +
                " ORDER BY appointment_date";

        return SQL;
    }

    public static String QUERY_TO_FETCH_PENDING_APPROVAL_DETAIL_BY_ID =
            "SELECT" +
                    " a.id as appointmentId," +                                                  //[0]
                    " a.appointmentDate as appointmentDate," +                                   //[1]
                    " a.appointmentNumber as appointmentNumber," +                               //[2]
                    " DATE_FORMAT(a.appointmentTime, '%h:%i %p') as appointmentTime," +          //[3]
                    " p.eSewaId as esewaId," +                                                   //[4]
                    " CASE WHEN" +
                    " (hpi.registrationNumber IS NULL)" +
                    " THEN 'N/A'" +
                    " ELSE" +
                    " hpi.registrationNumber" +
                    " END as registrationNumber," +                                              //[5]
                    " p.name as patientName," +                                                  //[6]
                    " p.gender as patientGender," +                                              //[7]
                    " p.dateOfBirth as patientDob," +                                            //[8]
                    " hpi.isRegistered as isRegistered," +                                       //[9]
                    " p.mobileNumber as mobileNumber," +                                        //[10]
                    " sp.name as specializationName," +                                         //[11]
                    " atd.transactionNumber as transactionNumber," +                            //[12]
                    " COALESCE(atd.appointmentAmount,0) as appointmentAmount," +                //[13]
                    " d.name as doctorName," +                                                  //[14]
                    " a.isSelf as isSelf," +                                                    //[15]
                    " h.name as hospitalName," +                                                //[16]
                    " a.appointmentModeId.name as appointmentMode," +                          //[17]
                    " da.fileUri as fileUri," +                                                //[18]
                    " d.salutation as doctorSalutation," +                                      //[19]
                    " da.fileUri as fileUri," +                                                 //[20]
                    " d.id as doctorId," +                                                      //[21]
                    " sp.id as specializationId," +                                             //[22]
                    " a.isFollowUp as followUp," +                                               //[23]
                    " hpi.hospitalNumber as hospitalNumber" +
                    " FROM Appointment a" +
                    " LEFT JOIN AppointmentDoctorInfo ad ON a.id = ad.appointment.id" +
                    " LEFT JOIN Patient p ON p.id = a.patientId" +
                    " LEFT JOIN HospitalPatientInfo hpi ON hpi.patient.id =p.id AND hpi.hospital.id = a.hospitalId.id" +
                    " LEFT JOIN Doctor d ON d.id = ad.doctor.id" +
                    " LEFT JOIN DoctorAvatar da ON d.id= da.doctorId.id" +
                    " LEFT JOIN Specialization sp ON sp.id= ad.specialization.id" +
                    " LEFT JOIN Hospital h ON h.id= a.hospitalId.id" +
                    " LEFT JOIN PatientMetaInfo pi ON p.id= pi.patient.id AND pi.status='Y'" +
                    " LEFT JOIN AppointmentTransactionDetail atd ON a.id = atd.appointment.id" +
                    " WHERE " +
                    " a.status='PA'" +
                    " AND a.id=:appointmentId";

    public static String QUERY_TO_REFUNDED_DETAIL_BY_ID =
            "SELECT" +
                    " a.appointmentDate as appointmentDate," +
                    " DATE_FORMAT(a.appointmentTime, '%h:%i %p') as appointmentTime," +
                    " a.appointmentNumber as appointmentNumber," +
                    " h.name as hospitalName," +
                    " p.name as patientName," +
                    " CASE WHEN" +
                    " (hpi.registrationNumber IS NULL)" +
                    " THEN 'N/A'" +
                    " ELSE" +
                    " hpi.registrationNumber" +
                    " END as registrationNumber," +
                    " p.gender as gender," +
                    " CASE WHEN" +
                    " (p.eSewaId IS NULL)" +
                    " THEN 'N/A'" +
                    " ELSE" +
                    " p.eSewaId" +
                    " END as eSewaId," +
                    " p.mobileNumber as mobileNumber," +
                    " CASE WHEN" +
                    " (d.salutation is null)" +
                    " THEN d.name" +
                    " ELSE" +
                    " CONCAT_WS(' ',d.salutation, d.name)" +
                    " END as doctorName," +
                    " s.name as specializationName," +
                    " atd.transactionNumber as transactionNumber," +
                    " DATE_FORMAT(ard.cancelledDate,'%M %d, %Y at %h:%i %p') as cancelledDate," +
                    " ard.refundAmount as refundAmount," +
                    " atd.appointmentAmount as appointmentCharge," +
                    " a.appointmentModeId.name as appointmentMode," +
                    " hpi.isRegistered as isRegistered," +
                    QUERY_TO_CALCULATE_PATIENT_AGE + "," +
                    " dv.fileUri as fileUri" +
                    " FROM" +
                    " AppointmentRefundDetail ard" +
                    " LEFT JOIN Appointment a ON a.id=ard.appointmentId.id" +
                    " LEFT JOIN AppointmentDoctorInfo ad ON a.id = ad.appointment.id" +
                    " LEFT JOIN Hospital h ON h.id=a.hospitalId.id" +
                    " LEFT JOIN Patient p ON p.id=a.patientId.id" +
                    " LEFT JOIN HospitalPatientInfo hpi ON hpi.patient.id =p.id AND hpi.hospital.id = a.hospitalId.id" +
                    " LEFT JOIN Doctor d ON d.id=ad.doctor.id" +
                    " LEFT JOIN DoctorAvatar dv ON dv.doctorId.id = d.id" +
                    " LEFT JOIN Specialization s ON s.id=ad.specialization.id" +
                    " LEFT JOIN AppointmentTransactionDetail atd ON atd.appointment.id =a.id" +
                    " WHERE ard.appointmentId.id=:appointmentId" +
                    " AND ard.status='PA'";


    private static String SELECT_CLAUSE_TO_GET_AMOUNT_AND_APPOINTMENT_COUNT =
            "SELECT" +
                    " COUNT(a.id)," +
                    " COALESCE(SUM(atd.appointmentAmount ),0)" +
                    " FROM Appointment a" +
                    " LEFT JOIN AppointmentDoctorInfo ad ON a.id = ad.appointment.id" +
                    " LEFT JOIN Patient p ON a.patientId.id=p.id" +
                    " LEFT JOIN HospitalPatientInfo hpi ON hpi.patient.id =p.id AND hpi.hospital.id = a.hospitalId.id" +
                    " LEFT JOIN Doctor d ON d.id = ad.doctor.id" +
                    " LEFT JOIN Specialization sp ON sp.id = ad.specialization.id" +
                    " LEFT JOIN Hospital h ON a.hospitalId.id=h.id" +
                    " LEFT JOIN PatientMetaInfo pi ON pi.patient.id=p.id AND pi.status='Y'" +
                    " LEFT JOIN AppointmentTransactionDetail atd ON a.id = atd.appointment.id" +
                    " WHERE" +
                    " sp.status!='D'" +
                    " AND d.status!='D'" +
                    " AND h.id=:hospitalId";

    private static String SELECT_CLAUSE_TO_GET_TOTAL_AMOUNT =
            "SELECT" +
                    " COALESCE (SUM(atd.appointmentAmount),0) - COALESCE(SUM(ard.refundAmount),0 ) as totalAmount" +
                    " FROM Appointment a" +
                    " LEFT JOIN AppointmentDoctorInfo ad ON a.id = ad.appointment.id" +
                    " LEFT JOIN Patient p ON a.patientId.id=p.id" +
                    " LEFT JOIN HospitalPatientInfo hpi ON hpi.patient.id =p.id AND hpi.hospital.id = a.hospitalId.id" +
                    " LEFT JOIN Doctor d ON d.id = ad.doctor.id" +
                    " LEFT JOIN Specialization sp ON sp.id = ad.specialization.id" +
                    " LEFT JOIN Hospital h ON a.hospitalId.id = h.id" +
                    " LEFT JOIN PatientMetaInfo pi ON pi.patient.id=p.id AND pi.status='Y'" +
                    " LEFT JOIN AppointmentTransactionDetail atd ON a.id = atd.appointment.id" +
                    " LEFT JOIN AppointmentRefundDetail ard ON ard.appointmentId=a.id AND ard.status='A'" +
                    " WHERE" +
                    " sp.status!='D'" +
                    " AND d.status!='D'" +
                    " AND h.id=:hospitalId";

    public static String QUERY_TO_FETCH_TOTAL_APPOINTMENT_AMOUNT(AppointmentLogSearchDTO searchRequestDTO) {
        String query = SELECT_CLAUSE_TO_GET_TOTAL_AMOUNT;

        return QUERY_TO_SEARCH_BY_DATES(query, searchRequestDTO);
    }


    public static String QUERY_TO_FETCH_BOOKED_APPOINTMENT(AppointmentLogSearchDTO searchRequestDTO) {
        String query = SELECT_CLAUSE_TO_GET_AMOUNT_AND_APPOINTMENT_COUNT +
                " AND a.status='PA' AND a.isFollowUp = 'N'";

        return QUERY_TO_SEARCH_BY_DATES(query, searchRequestDTO);
    }

    public static String QUERY_TO_FETCH_BOOKED_APPOINTMENT_WITH_FOLLOW_UP
            (AppointmentLogSearchDTO searchRequestDTO) {

        String query = SELECT_CLAUSE_TO_GET_AMOUNT_AND_APPOINTMENT_COUNT +
                " AND a.status='PA' AND a.isFollowUp = 'Y'";

        return QUERY_TO_SEARCH_BY_DATES(query, searchRequestDTO);
    }

    public static String QUERY_TO_FETCH_CHECKED_IN_APPOINTMENT(AppointmentLogSearchDTO searchRequestDTO) {
        String query = SELECT_CLAUSE_TO_GET_AMOUNT_AND_APPOINTMENT_COUNT +
                " AND a.status='A' AND a.isFollowUp = 'N'";

        return QUERY_TO_SEARCH_BY_DATES(query, searchRequestDTO);
    }

    public static String QUERY_TO_FETCH_CHECKED_IN_APPOINTMENT_WITH_FOLLOW_UP(AppointmentLogSearchDTO searchRequestDTO) {
        String query = SELECT_CLAUSE_TO_GET_AMOUNT_AND_APPOINTMENT_COUNT +
                " AND a.status='A' AND a.isFollowUp = 'Y'";

        return QUERY_TO_SEARCH_BY_DATES(query, searchRequestDTO);
    }

    public static String QUERY_TO_FETCH_CANCELLED_APPOINTMENT(AppointmentLogSearchDTO searchRequestDTO) {
        String query = SELECT_CLAUSE_TO_GET_AMOUNT_AND_APPOINTMENT_COUNT +
                " AND a.status='C' AND a.isFollowUp = 'N'";

        return QUERY_TO_SEARCH_BY_DATES(query, searchRequestDTO);
    }

    public static String QUERY_TO_FETCH_CANCELLED_APPOINTMENT_WITH_FOLLOW_UP(AppointmentLogSearchDTO searchRequestDTO) {
        String query = SELECT_CLAUSE_TO_GET_AMOUNT_AND_APPOINTMENT_COUNT +
                " AND a.status='C' AND a.isFollowUp = 'Y'";

        return QUERY_TO_SEARCH_BY_DATES(query, searchRequestDTO);
    }

    private static String SELECT_CLAUSE_TO_FETCH_REFUNDED_APPOINTMENT =
            "SELECT" +
                    " COUNT(a.id)," +
                    " COALESCE (SUM(ard.refundAmount ),0) as amount" +
                    " FROM Appointment a" +
                    " LEFT JOIN AppointmentDoctorInfo ad ON a.id = ad.appointment.id" +
                    " LEFT JOIN Patient p ON a.patientId.id=p.id" +
                    " LEFT JOIN HospitalPatientInfo hpi ON hpi.patient.id =p.id AND hpi.hospital.id = a.hospitalId.id" +
                    " LEFT JOIN Doctor d ON d.id = ad.doctor.id" +
                    " LEFT JOIN Specialization sp ON sp.id = ad.specialization.id" +
                    " LEFT JOIN Hospital h ON a.hospitalId.id=h.id" +
                    " LEFT JOIN PatientMetaInfo pi ON pi.patient.id=p.id AND pi.status='Y'" +
                    " LEFT JOIN AppointmentTransactionDetail atd ON a.id = atd.appointment.id" +
                    " LEFT JOIN AppointmentRefundDetail ard ON ard.appointmentId=a.id AND ard.status='A'" +
                    " WHERE" +
                    " a.status='RE'" +
                    " AND h.id=:hospitalId" +
                    " AND sp.status!='D'" +
                    " AND d.status!='D'";

    public static String QUERY_TO_FETCH_REFUNDED_APPOINTMENT(AppointmentLogSearchDTO searchRequestDTO) {
        String query = SELECT_CLAUSE_TO_FETCH_REFUNDED_APPOINTMENT +
                " AND a.isFollowUp = 'N'";

        return QUERY_TO_SEARCH_BY_DATES(query, searchRequestDTO);
    }

    public static String QUERY_TO_FETCH_REFUNDED_APPOINTMENT_WITH_FOLLOW_UP(AppointmentLogSearchDTO searchRequestDTO) {
        String query = SELECT_CLAUSE_TO_FETCH_REFUNDED_APPOINTMENT +
                " AND a.isFollowUp = 'Y'";

        return QUERY_TO_SEARCH_BY_DATES(query, searchRequestDTO);
    }

    private static String SELECT_CLAUSE_TO_FETCH_REVENUE_REFUNDED_APPOINTMENT =
            "SELECT" +
                    " COUNT(a.id)," +
                    " (COALESCE(SUM(atd.appointmentAmount ),0) - COALESCE(SUM(ard.refundAmount ),0)) as amount" +
                    " FROM Appointment a" +
                    " LEFT JOIN AppointmentDoctorInfo ad ON a.id = ad.appointment.id" +
                    " LEFT JOIN Patient p ON a.patientId.id=p.id" +
                    " LEFT JOIN HospitalPatientInfo hpi ON hpi.patient.id =p.id AND hpi.hospital.id = a.hospitalId.id" +
                    " LEFT JOIN Doctor d ON d.id = ad.doctor.id" +
                    " LEFT JOIN Specialization sp ON sp.id = ad.specialization.id" +
                    " LEFT JOIN Hospital h ON a.hospitalId.id=h.id" +
                    " LEFT JOIN PatientMetaInfo pi ON pi.patient.id=p.id AND pi.status='Y'" +
                    " LEFT JOIN AppointmentTransactionDetail atd ON a.id = atd.appointment.id" +
                    " LEFT JOIN AppointmentRefundDetail ard ON ard.appointmentId=a.id AND ard.status='A'" +
                    " WHERE" +
                    " a.status='RE'" +
                    " AND h.id=:hospitalId" +
                    " AND sp.status!='D'" +
                    " AND d.status!='D'";

    public static String QUERY_TO_FETCH_REVENUE_REFUNDED_APPOINTMENT(AppointmentLogSearchDTO searchRequestDTO) {
        String query = SELECT_CLAUSE_TO_FETCH_REVENUE_REFUNDED_APPOINTMENT +
                " AND a.isFollowUp = 'N'";

        return QUERY_TO_SEARCH_BY_DATES(query, searchRequestDTO);
    }

    public static String QUERY_TO_FETCH_REVENUE_REFUNDED_APPOINTMENT_WITH_FOLLOW_UP(AppointmentLogSearchDTO searchRequestDTO) {
        String query = SELECT_CLAUSE_TO_FETCH_REVENUE_REFUNDED_APPOINTMENT +
                " AND a.isFollowUp = 'Y'";

        return QUERY_TO_SEARCH_BY_DATES(query, searchRequestDTO);
    }

    private static String QUERY_TO_SEARCH_BY_DATES(String query, AppointmentLogSearchDTO searchRequestDTO) {

        if (!ObjectUtils.isEmpty(searchRequestDTO.getFromDate())
                && !ObjectUtils.isEmpty(searchRequestDTO.getToDate()))
            query += " AND (a.appointmentDate BETWEEN '" + utilDateToSqlDate(searchRequestDTO.getFromDate())
                    + "' AND '" + utilDateToSqlDate(searchRequestDTO.getToDate()) + "')";

        if (!Objects.isNull(searchRequestDTO.getStatus()) && !searchRequestDTO.getStatus().equals(""))
            query += " AND a.status = '" + searchRequestDTO.getStatus() + "'";

        if (!Objects.isNull(searchRequestDTO.getAppointmentId()))
            query += " AND a.id = " + searchRequestDTO.getAppointmentId();

        if (!Objects.isNull(searchRequestDTO.getPatientMetaInfoId()))
            query += " AND pi.id = " + searchRequestDTO.getPatientMetaInfoId();

        if (!Objects.isNull(searchRequestDTO.getSpecializationId()))
            query += " AND sp.id = " + searchRequestDTO.getSpecializationId();

        if (!ObjectUtils.isEmpty(searchRequestDTO.getPatientType()))
            query += " AND hpi.isRegistered = '" + searchRequestDTO.getPatientType() + "'";

        if (!ObjectUtils.isEmpty(searchRequestDTO.getAppointmentCategory()))
            query += " AND a.isSelf = '" + searchRequestDTO.getAppointmentCategory() + "'";

        if (!Objects.isNull(searchRequestDTO.getDoctorId()))
            query += " AND d.id = " + searchRequestDTO.getDoctorId();

        return query;
    }

    public static String QUERY_TO_GET_CANCELLED_APPOINTMENT =
            "SELECT" +
                    " a" +
                    " FROM" +
                    " Appointment a " +
                    " LEFT JOIN AppointmentTransactionDetail atd ON atd.appointment.id=a.id " +
                    " WHERE atd.transactionNumber=:transactionNumber" +
                    " AND a.patientId.eSewaId =:esewaId";
}
